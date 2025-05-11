package com.backend.centroautorizador.bbdd.service;

import com.backend.centroautorizador.bbdd.model.Balance;
import com.backend.centroautorizador.bbdd.model.Payment;
import com.backend.centroautorizador.bbdd.repo.PaymentRepository;
import com.backend.centroautorizador.rabbitmq.publisher.RabbitMQJsonProducer;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import io.opentelemetry.api.trace.SpanKind;


import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    private final com.backend.centroautorizador.bbdd.repo.PaymentRepository paymentRepository;
    private final com.backend.centroautorizador.bbdd.service.StateService stateService;
    private final com.backend.centroautorizador.bbdd.service.BalanceService balanceService;
    private final RabbitMQJsonProducer rabbitMQProducer;

    @Autowired
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("payment-tracer");

    // Métricas OpenTelemetry
    private final Meter meter;
    private LongCounter paymentCounter;

    @Autowired
    public PaymentService(
            com.backend.centroautorizador.bbdd.repo.PaymentRepository paymentRepository,
            com.backend.centroautorizador.bbdd.service.StateService stateService,
            com.backend.centroautorizador.bbdd.service.BalanceService balanceService,
            RabbitMQJsonProducer rabbitMQProducer,
            Meter meter) {
        this.paymentRepository = paymentRepository;
        this.stateService = stateService;
        this.balanceService = balanceService;
        this.rabbitMQProducer = rabbitMQProducer;
        this.meter = meter;
        LOGGER.info("PaymentService Ready");
    }

    @PostConstruct
    public void initMetrics() {
        paymentCounter = meter.counterBuilder("payment_processed")
                .setDescription("Number of payments processed")
                .setUnit("1")
                .build();
    }

    @Scheduled(fixedRate = 5000)
    public void checkForNewInserts() {
        Span transaction = tracer.spanBuilder("scheduled-payment-check")
                .setSpanKind(SpanKind.INTERNAL)
                .startSpan();
        try (Scope txScope = transaction.makeCurrent()) {
            String traceId = transaction.getSpanContext().getTraceId();
            MDC.put("traceId", traceId);
            LOGGER.info("PaymentService Searching Pending Payments");

            List<Payment> newRecords = paymentRepository.findByState(stateService.findByState(1));

            if (!newRecords.isEmpty()) {
                for (Payment record : newRecords) {

                    Span span = tracer.spanBuilder("process-payment").startSpan();
                    Span parentSpan = tracer.spanBuilder("process-payment").startSpan();
                    try (Scope scope = span.makeCurrent()) {
                        // Inyectar traceId en logs
                        String subTraceId = span.getSpanContext().getTraceId();
                        MDC.put("traceId", subTraceId);

                        span.setAttribute("payment.id", record.getId());
                        span.setAttribute("sender.id", record.getSender().getId());
                        span.setAttribute("receiver.id", record.getReceiver().getId());
                        span.setAttribute("amount", record.getAmount());
                        span.setAttribute("initial.state", record.getState().getDescription());
                        parentSpan.setAttribute("payment.id", record.getId());
                        parentSpan.setAttribute("amount", record.getAmount());

                        LOGGER.info("Processing new record -> {}", record);

                        Span fetchStateSpan = tracer.spanBuilder("fetch-payment-state").startSpan();
                        record.setState(stateService.findByState(2).orElse(record.getState()));
                        paymentRepository.save(record);
                        rabbitMQProducer.sendJsonMessage(record);
                        fetchStateSpan.end();

                        TimeUnit.SECONDS.sleep(5);

                        Span balanceSpan = tracer.spanBuilder("calculate-and-update-balances").startSpan();
                        double amount = record.getAmount();
                        Balance sender = record.getSender().getCc();
                        Balance receiver = record.getReceiver().getCc();
                        double difference = sender.getBalance() - amount;

                        String result;
                        if (record.getSender().getId().equals(record.getReceiver().getId())) {
                            if (amount >= 0) {
                                sender.setBalance(sender.getBalance() + amount);
                                balanceService.insertBalance(sender);
                                record.setState(stateService.findByState(3).orElse(record.getState()));
                                result = "accepted";
                                span.setAttribute("payment.result", "accepted");
                            } else {
                                record.setState(stateService.findByState(4).orElse(record.getState()));
                                result = "rejected";
                                span.setAttribute("payment.result", "rejected");
                                span.setAttribute("payment.reject_reason", "invalid_amount");
                            }
                        } else {
                            if (amount >= 0 && difference >= 0) {
                                sender.setBalance(difference);
                                receiver.setBalance(receiver.getBalance() + amount);
                                balanceService.insertBalance(sender);
                                balanceService.insertBalance(receiver);
                                record.setState(stateService.findByState(3).orElse(record.getState()));
                                result = "accepted";
                                span.setAttribute("payment.result", "accepted");
                            } else {
                                record.setState(stateService.findByState(4).orElse(record.getState()));
                                result = "rejected";
                                span.setAttribute("payment.result", "rejected");
                                if (amount < 0) {
                                    span.setAttribute("payment.reject_reason", "invalid_amount");
                                } else {
                                    span.setAttribute("payment.reject_reason", "no_balance");
                                }
                            }
                        }

                        paymentCounter.add(1, Attributes.of(AttributeKey.stringKey("result"), result));
                        balanceSpan.end();

                        Span publishSpan = tracer.spanBuilder("publish-to-rabbitmq").startSpan();
                        paymentRepository.save(record);
                        rabbitMQProducer.sendJsonMessage(record);
                        publishSpan.end();

                        span.setAttribute("final.state", record.getState().getDescription());
                        LOGGER.info("Record processed -> {}", record);

                    } catch (Exception ex) {
                        span.recordException(ex);
                        span.setStatus(StatusCode.ERROR, "Error al procesar pago");
                        LOGGER.error("❌ Error procesando pago", ex);
                    } finally {
                        span.end();
                        parentSpan.end();
                        MDC.remove("traceId");
                    }
                }
            }

        } catch (Exception e) {
            transaction.recordException(e);
            transaction.setStatus(StatusCode.ERROR, "Error al ejecutar tarea programada");
            LOGGER.error("❌ Error general en la tarea de pagos", e);
        } finally {
            transaction.end();
            MDC.remove("traceId");
        }
    }

}
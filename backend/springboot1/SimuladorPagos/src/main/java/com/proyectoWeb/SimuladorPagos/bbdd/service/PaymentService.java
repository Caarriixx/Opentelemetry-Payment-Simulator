package com.proyectoWeb.SimuladorPagos.bbdd.service;

import com.proyectoWeb.SimuladorPagos.bbdd.model.Payment;
import com.proyectoWeb.SimuladorPagos.bbdd.model.PaymentMessage;
import com.proyectoWeb.SimuladorPagos.bbdd.model.State;
import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import com.proyectoWeb.SimuladorPagos.bbdd.repository.PaymentRepository;
//import com.backend.centroautorizador.rabbitmq.publisher.RabbitMQProducer;
import com.proyectoWeb.SimuladorPagos.elastic.entity.Paymente;
import com.proyectoWeb.SimuladorPagos.elastic.service.Paymentservice;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;

import java.util.*;


@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    // Usamos el tracer global, conectado al javaagent de Elastic APM
    private final Tracer tracer =
            GlobalOpenTelemetry.getTracer("make-payment");

    private final PaymentRepository paymentRepository;
    private final StateService stateService;
    private final Paymentservice paymentservice;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          StateService stateService,
                          Paymentservice paymentservice) {
        this.paymentRepository = paymentRepository;
        this.stateService = stateService;
        this.paymentservice = paymentservice;
    }

    public Iterable<Payment> getPayments(User user) {
        Collection<Payment> aux = paymentRepository.findByReceiver(user);
        aux.addAll(paymentRepository.findBySender(user));
        return new HashSet<>(aux);
    }

    public void insertPayment(PaymentMessage paymentMessage) {
        // Iniciamos el span con el tracer global
        Span span = tracer.spanBuilder("Insert Payment")
                .setAttribute("custom.sender.id", paymentMessage.getSender().getId())
                .setAttribute("custom.receiver.id", paymentMessage.getReceiver().getId())
                .setAttribute("custom.amount", paymentMessage.getAmount())
                .setAttribute("custom.state", paymentMessage.getState())
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            String traceId = span.getSpanContext().getTraceId();
            MDC.put("traceId", traceId);

            span.addEvent("Payment request received");

            span.setAttribute("custom.sender.id", paymentMessage.getSender().getId());
            span.setAttribute("custom.receiver.id", paymentMessage.getReceiver().getId());
            span.setAttribute("custom.amount", paymentMessage.getAmount());
            span.setAttribute("custom.state", paymentMessage.getState());
            if (paymentMessage.getDate() != null) {
                long epochMillis = paymentMessage.getDate().getTime();
                span.setAttribute(AttributeKey.longKey("custom.date.epochMillis"), epochMillis);
            }

            LOGGER.info("📥 Procesando pago con estado {}", paymentMessage.getState());

            State estado = stateService.findByState(paymentMessage.getState())
                    .orElseGet(() -> {
                        LOGGER.warn("⚠ No se encontró estado {}, usando uno por defecto",
                                paymentMessage.getState());
                        return stateService.listStates().iterator().next();
                    });
            LOGGER.info("✔ Estado usado: {}", estado);

            Payment payment = new Payment(
                    paymentMessage.getAmount(),
                    paymentMessage.getDate(),
                    estado,
                    paymentMessage.getSender(),
                    paymentMessage.getReceiver()
            );

            Paymente paymentElas = new Paymente(
                    paymentMessage.getSender().getId(),
                    paymentMessage.getSender().getId(),
                    paymentMessage.getReceiver().getId(),
                    paymentMessage.getAmount(),
                    paymentMessage.getState(),
                    paymentMessage.getDate()
            );

            LOGGER.info("💾 Insertando en Elasticsearch y MySQL: {}", payment);
            paymentservice.savePayment(paymentElas);
            paymentRepository.save(payment);

        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, "Error procesando pago");
            span.recordException(e);
            LOGGER.error("❌ Error insertando pago", e);
        } finally {
            span.end();
            MDC.remove("traceId");
        }
    }
}

/*
    @Scheduled(fixedRate = 10000)
    public void checkForNewInserts() {
        List<Payment> newRecords = paymentRepository.findByState(stateService.findByState(2));

        if (!newRecords.isEmpty()) {
            for (Payment record : newRecords) {
                // Procesa el registro nuevo
                LOGGER.info(String.format("Procesando nuevo registro ->  %s", record));
//                System.out.println("Procesando nuevo registro con ID: " + record.getId());

                // Aquí pones la lógica que quieras al detectar la inserción

                // Marcar como procesado
                Optional<State> state = stateService.findByState(4);
                record.setState(state.orElse(record.getState()));
                paymentRepository.save(record);
                LOGGER.info(String.format("Registro procesado ->  %s", record));
            }
        }
    }*/
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
        Span span = tracer.spanBuilder("Insert Payment")
                .setAttribute("payment.sender.id", paymentMessage.getSender().getId())
                .setAttribute("payment.receiver.id", paymentMessage.getReceiver().getId())
                .setAttribute("payment.amount", paymentMessage.getAmount())
                .setAttribute("payment.state", paymentMessage.getState())
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            String traceId = span.getSpanContext().getTraceId();
            MDC.put("traceId", traceId);
            span.addEvent("Payment request received");

            // Validar amount > 0
            if (paymentMessage.getAmount() <= 0) {
                span.setStatus(StatusCode.ERROR, "Amount must be greater than 0");
                span.setAttribute("payment.error", "invalid_amount");
                throw new IllegalArgumentException("Amount must be greater than 0.");
            }

            // Validar fecha
            if (paymentMessage.getDate() == null) {
                span.setStatus(StatusCode.ERROR, "Missing or invalid date");
                span.setAttribute("payment.error", "invalid_date");
                throw new IllegalArgumentException("Payment must include a valid date.");
            }

            span.setAttribute(AttributeKey.longKey("payment.date.epochMillis"), paymentMessage.getDate().getTime());

            // Validar existencia de sender y receiver
            Optional<User> senderOpt = Optional.ofNullable(paymentMessage.getSender());
            Optional<User> receiverOpt = Optional.ofNullable(paymentMessage.getReceiver());

            if (senderOpt.isEmpty() || senderOpt.get().getId() == null) {
                span.setStatus(StatusCode.ERROR, "Sender not provided");
                span.setAttribute("payment.error", "sender_missing");
                throw new IllegalArgumentException("Sender must be provided.");
            }

            if (receiverOpt.isEmpty() || receiverOpt.get().getId() == null) {
                span.setStatus(StatusCode.ERROR, "Receiver not provided");
                span.setAttribute("payment.error", "receiver_missing");
                throw new IllegalArgumentException("Receiver must be provided.");
            }

            User sender = senderOpt.get();
            User receiver = receiverOpt.get();

            // Validar estado entre 0 y 4
            if (paymentMessage.getState() < 0 || paymentMessage.getState() > 4) {
                span.setStatus(StatusCode.ERROR, "Invalid payment state");
                span.setAttribute("payment.error", "invalid_state");
                throw new IllegalArgumentException("State must be between 0 and 4.");
            }

            // Validar que el sender tiene saldo suficiente
            if (sender.getCc() == null || sender.getCc().getBalance() < paymentMessage.getAmount()) {
                span.setStatus(StatusCode.ERROR, "Insufficient balance");
                span.setAttribute("payment.error", "insufficient_funds");
                span.setAttribute("payment.balance.sender", sender.getCc() != null ? sender.getCc().getBalance() : 0.0);
                throw new IllegalArgumentException("Sender has insufficient balance.");
            }

            // Obtener el estado
            State estado = stateService.findByState(paymentMessage.getState())
                    .orElseThrow(() -> {
                        span.setStatus(StatusCode.ERROR, "State not found");
                        span.setAttribute("payment.error", "state_not_found");
                        return new IllegalArgumentException("Specified state not found.");
                    });

            Payment payment = new Payment(
                    paymentMessage.getAmount(),
                    paymentMessage.getDate(),
                    estado,
                    sender,
                    receiver
            );

            Paymente paymentElas = new Paymente(
                    sender.getId(),
                    sender.getId(),
                    receiver.getId(),
                    paymentMessage.getAmount(),
                    paymentMessage.getState(),
                    paymentMessage.getDate()
            );

            span.setAttribute("payment.status", "accepted");
            LOGGER.info("💾 Guardando pago en Elasticsearch y MySQL");
            paymentservice.savePayment(paymentElas);
            paymentRepository.save(payment);

        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, "Payment processing failed");
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
package com.backend.centroautorizador.rabbitmq.publisher;

import com.backend.centroautorizador.bbdd.model.Payment;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapPropagator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for publishing JSON messages to RabbitMQ with traceparent propagation.
 */
@Service
public class RabbitMQJsonProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingJsonKey;

    @Autowired
    private Tracer tracer;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonProducer.class);

    private final RabbitTemplate rabbitTemplate;

    // W3C TextMap propagator for traceparent
    private final TextMapPropagator propagator =
            GlobalOpenTelemetry.getPropagators().getTextMapPropagator();

    @Autowired
    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(Payment message) {
        Span span = tracer.spanBuilder("rabbitmq-send-payment").startSpan();
        try (var scope = span.makeCurrent()) {
            // Inyectar traceId en logs
            String traceId = span.getSpanContext().getTraceId();
            MDC.put("traceId", traceId);

            span.setAttribute("payment.id", message.getId());
            span.setAttribute("amount", message.getAmount());
            span.setAttribute("sender.id", message.getSender().getId());
            span.setAttribute("receiver.id", message.getReceiver().getId());
            span.setAttribute("state", message.getState().getDescription());
            span.setAttribute("exchange", exchange);
            span.setAttribute("routingKey", routingJsonKey);

            LOGGER.info("📤 Enviando mensaje a RabbitMQ: {}", message);

            // Inyectamos traceparent en los headers usando MessagePostProcessor
            MessagePostProcessor addTrace = msg -> {
                // Propagación de traceparent
                propagator.inject(
                        Context.current(),
                        msg.getMessageProperties(),
                        (props, key, value) -> props.setHeader(key, value)
                );

                // Forzar tipo compatible con Spring Boot 1
                msg.getMessageProperties().setHeader("__TypeId__", "com.proyectoWeb.SimuladorPagos.bbdd.model.Payment");

                return msg;
            };
            rabbitTemplate.convertAndSend(exchange, routingJsonKey, message, addTrace);
        } finally {
            // Limpiar MDC y terminar span
            MDC.remove("traceId");
            span.end();
        }
    }
}
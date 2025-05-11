package com.proyectoWeb.SimuladorPagos.rabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoWeb.SimuladorPagos.bbdd.model.Payment;
import com.proyectoWeb.SimuladorPagos.bbdd.service.PaymentService;
import com.proyectoWeb.SimuladorPagos.bbdd.service.UserService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;

import io.opentelemetry.context.propagation.TextMapPropagator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private Tracer tracer;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    private final ObjectMapper mapper = new ObjectMapper();

    // W3C TextMap propagator
    private final TextMapPropagator propagator =
            GlobalOpenTelemetry.getPropagators().getTextMapPropagator();

    // Getter para extraer traceparent del mapa de headers
    private static final TextMapGetter<Map<String, Object>> getter =
            new TextMapGetter<>() {
                @Override
                public Iterable<String> keys(Map<String, Object> carrier) {
                    return carrier.keySet();
                }
                @Override
                public String get(Map<String, Object> carrier, String key) {
                    Object val = carrier.get(key);
                    return val != null ? val.toString() : null;
                }
            };

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(Message rawMessage) {
        // Extraemos headers y reconstruimos contexto padre
        Map<String, Object> headers = rawMessage.getMessageProperties().getHeaders();
        Context parentCtx = propagator.extract(Context.current(), headers, getter);

        // Iniciamos span con contexto padre
        Span span = tracer.spanBuilder("rabbitmq-consume-payment")
                .setParent(parentCtx)
                .startSpan();

        try (var scope = span.makeCurrent()) {
            // Inyectar traceId en logs
            String traceId = span.getSpanContext().getTraceId();
            MDC.put("traceId", traceId);

            // Reconstruir payload
            Payment payment = mapper.readValue(rawMessage.getBody(), Payment.class);

            // Atributos de trazabilidad
            span.setAttribute("payment.id", payment.getId());
            span.setAttribute("amount", payment.getAmount());
            span.setAttribute("sender.id", payment.getSender().getId());
            span.setAttribute("receiver.id", payment.getReceiver().getId());
            span.setAttribute("state", payment.getState().toString());

            LOGGER.info("📥 Mensaje recibido desde RabbitMQ: {}", payment);

            // Simular procesamiento
            TimeUnit.SECONDS.sleep(5);

            // Notificaciones y actualizaciones
            enviarNotificacionDePago(payment);
            actualizarInfo(payment);

        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, "Error procesando mensaje RabbitMQ");
            LOGGER.error("❌ Error processing RabbitMQ message", e);
        } finally {
            span.end();
            MDC.remove("traceId");
        }
    }

    private void enviarNotificacionDePago(Payment payment) {
        Span notifySpan = tracer.spanBuilder("consume-payment-notification").startSpan();
        try (var scope = notifySpan.makeCurrent()) {
            String userId = payment.getSender().getId().toString();
            notifySpan.setAttribute("payment.id", payment.getId());
            notifySpan.setAttribute("amount", payment.getAmount());

            String destino = "/topic/payment/notification/" + userId;
            messagingTemplate.convertAndSend(destino, payment);
            LOGGER.info("Notificación de pago enviada a {}", destino);
        } finally {
            notifySpan.end();
        }
    }

    private void actualizarInfo(Payment payment) {
        String userId = payment.getSender().getId().toString();
        String userDest = "/topic/user/" + userId + "/" + userId;
        String paymentsDest = "/topic/payment/user/" + userId;

        var user = userService.findByID(Integer.valueOf(userId)).orElse(null);
        var payments = paymentService.getPayments(user);

        messagingTemplate.convertAndSend(userDest, user);
        messagingTemplate.convertAndSend(paymentsDest, payments);
    }
}

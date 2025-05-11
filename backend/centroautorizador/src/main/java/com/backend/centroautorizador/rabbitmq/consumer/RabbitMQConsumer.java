package com.backend.centroautorizador.rabbitmq.consumer;

import com.backend.centroautorizador.bbdd.model.Payment;
import com.backend.centroautorizador.rabbitmq.publisher.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Consumer service for handling messages from RabbitMQ.
 * <p>
 * This service listens to a specified RabbitMQ queue and processes incoming messages.
 * It is primarily used to handle {@link Payment} objects sent to the configured queue.
 * </p>
 */
@Service
public class RabbitMQConsumer {

    @Autowired
    private Tracer tracer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    /**
     * Listens for messages on the configured RabbitMQ queue and processes them.
     *
     * @param message The {@link Payment} message received from the queue.
     */
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(Payment message) {
        Span span = tracer.spanBuilder("rabbit-consume-payment").startSpan();
        try {
            span.setAttribute("payment.id", message.getId());
            span.setAttribute("amount", message.getAmount());
            span.setAttribute("sender.id", message.getSender().getId());
            span.setAttribute("receiver.id", message.getReceiver().getId());
            span.setAttribute("state", message.getState().getDescription());

            LOGGER.info("📥 Mensaje recibido desde RabbitMQ: {}", message);

            // Simular procesamiento
            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "Error procesando mensaje RabbitMQ");
            LOGGER.error("❌ Error while processing the message", e);
            Thread.currentThread().interrupt();
        } finally {
            span.end();
        }
    }

}

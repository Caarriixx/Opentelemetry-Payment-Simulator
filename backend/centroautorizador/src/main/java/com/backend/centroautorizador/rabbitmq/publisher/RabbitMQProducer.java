package com.backend.centroautorizador.rabbitmq.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Service for publishing plain text messages to RabbitMQ.
 * <p>
 * This class handles the publishing of plain text messages to a configured RabbitMQ
 * exchange and routing key. It is used to send simple string messages.
 * </p>
 */
@Service
public class RabbitMQProducer {

    /**
     * Name of the RabbitMQ exchange, injected from application properties.
     */
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    /**
     * Routing key for messages, injected from application properties.
     */
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructor for {@link RabbitMQProducer}.
     *
     * @param rabbitTemplate The {@link RabbitTemplate} to handle message publishing.
     */
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publishes a plain text message to RabbitMQ.
     *
     * @param message The plain text message to be published.
     */
    public void sendMessage(String message) {
        LOGGER.info(String.format("Message sent -> %s", message));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}


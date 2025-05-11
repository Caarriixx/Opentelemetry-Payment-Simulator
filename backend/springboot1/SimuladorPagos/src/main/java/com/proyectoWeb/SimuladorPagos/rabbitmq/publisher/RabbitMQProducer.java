package com.proyectoWeb.SimuladorPagos.rabbitmq.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service to publish text messages to RabbitMQ.
 * <p>
 * This service is responsible for sending plain text messages to a RabbitMQ exchange
 * using a specified routing key. It integrates with RabbitMQ via the {@link RabbitTemplate}.
 */
@Service
public class RabbitMQProducer {

//    @Value("${rabbitmq.exchange.name}")
//    private String exchange;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);
//
//    private RabbitTemplate rabbitTemplate;
//
//    /**
//     * Constructor for the {@link RabbitMQProducer}.
//     *
//     * @param rabbitTemplate The {@link RabbitTemplate} used to interact with RabbitMQ.
//     */
//    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    /**
//     * Sends a text message to RabbitMQ.
//     * <p>
//     * Publishes a plain text message to the RabbitMQ exchange specified in the
//     * {@code rabbitmq.exchange.name} property, using the routing key specified in the
//     * {@code rabbitmq.routing.key} property.
//     *
//     * @param message The text message to be sent to RabbitMQ.
//     */
//    public void sendMessage(String message) {
//        LOGGER.info(String.format("Message sent -> %s", message));
//        rabbitTemplate.convertAndSend(exchange, routingKey, message);
//    }
}

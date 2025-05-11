package com.proyectoWeb.SimuladorPagos.rabbitmq.publisher;

import com.proyectoWeb.SimuladorPagos.rabbitmq.model.Message;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service to publish JSON messages to RabbitMQ.
 * <p>
 * This service is responsible for sending JSON-formatted messages to a RabbitMQ exchange
 * using a specified routing key. It integrates with RabbitMQ through the {@link RabbitTemplate}.
 */
@Service
public class RabbitMQJsonProducer {

//    @Value("${rabbitmq.exchange.name}")
//    private String exchange;
//
//    @Value("${rabbitmq.routing.json.key}")
//    private String routingJsonKey;
//
//    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonProducer.class);
//
//    private RabbitTemplate rabbitTemplate;
//
//    /**
//     * Constructor for the {@link RabbitMQJsonProducer}.
//     *
//     * @param rabbitTemplate The {@link RabbitTemplate} used to interact with RabbitMQ.
//     */
//    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    /**
//     * Sends a JSON message to RabbitMQ.
//     * <p>
//     * This method publishes a JSON-formatted message to the RabbitMQ exchange specified in the
//     * {@code rabbitmq.exchange.name} property, using the routing key specified in the
//     * {@code rabbitmq.routing.json.key} property.
//     *
//     * @param message The {@link Message} object to be sent to RabbitMQ.
//     */
//    public void sendJsonMessage(Message message) {
//        LOGGER.info(String.format("Json message sent -> %s", message.toString()));
//        rabbitTemplate.convertAndSend(exchange, routingJsonKey, message);
//    }
}

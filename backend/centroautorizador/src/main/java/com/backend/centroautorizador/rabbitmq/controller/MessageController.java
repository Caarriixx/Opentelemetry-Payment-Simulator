package com.backend.centroautorizador.rabbitmq.controller;

import com.backend.centroautorizador.rabbitmq.publisher.RabbitMQProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for sending messages to RabbitMQ.
 * <p>
 * This controller provides an endpoint to publish messages to a RabbitMQ queue
 * through the {@link RabbitMQProducer}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "MessageController", description = "Endpoints for managing message publishing to RabbitMQ.")
public class MessageController {

    private final RabbitMQProducer producer;

    /**
     * Constructor for {@link MessageController}.
     *
     * @param producer The {@link RabbitMQProducer} to handle message publishing.
     */
    public MessageController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    /**
     * Publishes a message to RabbitMQ.
     *
     * @param message The message to be sent to the queue.
     * @return A {@link ResponseEntity} indicating the status of the operation.
     */
    @Operation(summary = "Publish a message to RabbitMQ", description = "Publishes a message to the configured RabbitMQ queue.")
    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        producer.sendMessage(message);
        return ResponseEntity.ok("Message sent...");
    }
}

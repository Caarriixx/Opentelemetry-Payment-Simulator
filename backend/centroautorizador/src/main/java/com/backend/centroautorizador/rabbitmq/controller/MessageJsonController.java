package com.backend.centroautorizador.rabbitmq.controller;

import com.backend.centroautorizador.bbdd.model.Payment;
import com.backend.centroautorizador.rabbitmq.publisher.RabbitMQJsonProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for sending JSON messages to RabbitMQ.
 * <p>
 * This controller provides an endpoint to publish JSON-structured messages to a RabbitMQ queue
 * using the {@link RabbitMQJsonProducer}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "MessageJsonController", description = "Endpoints for managing JSON message publishing to RabbitMQ.")
public class MessageJsonController {

    private final RabbitMQJsonProducer jsonProducer;

    /**
     * Constructor for {@link MessageJsonController}.
     *
     * @param jsonProducer The {@link RabbitMQJsonProducer} to handle JSON message publishing.
     */
    public MessageJsonController(RabbitMQJsonProducer jsonProducer) {
        this.jsonProducer = jsonProducer;
    }

    /**
     * Publishes a JSON message to RabbitMQ.
     *
     * @param message The {@link Payment} message to be sent to the queue.
     * @return A {@link ResponseEntity} indicating the status of the operation.
     */
    @Operation(summary = "Publish a JSON message to RabbitMQ", description = "Publishes a JSON-formatted message to the configured RabbitMQ queue.")
    @PostMapping("/publish")
    public ResponseEntity<String> sendJsonMessage(@RequestBody Payment message) {
        jsonProducer.sendJsonMessage(message);
        return ResponseEntity.ok("JSON message sent...");
    }
}


package com.proyectoWeb.SimuladorPagos.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ queues, exchanges, and bindings.
 * <p>
 * This class defines the setup for RabbitMQ components such as queues, exchanges,
 * bindings, and message converters. It ensures proper communication between
 * producers and consumers in the RabbitMQ system.
 */
@Configuration
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.queue.json.name}")
    private String jsonQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    /**
     * Defines the primary RabbitMQ queue.
     *
     * @return A new {@link Queue} object for the primary queue.
     */
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    /**
     * Defines the RabbitMQ JSON-specific queue.
     *
     * @return A new {@link Queue} object for the JSON queue.
     */
    @Bean
    public Queue jsonQueue() {
        return new Queue(jsonQueue);
    }

    /**
     * Defines the RabbitMQ topic exchange.
     *
     * @return A new {@link TopicExchange} object for message routing.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    /**
     * Binds the primary queue to the exchange with the specified routing key.
     *
     * @return A new {@link Binding} object connecting the primary queue and exchange.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    /**
     * Binds the JSON-specific queue to the exchange with the specified routing key.
     *
     * @return A new {@link Binding} object connecting the JSON queue and exchange.
     */
    @Bean
    public Binding jsonBinding() {
        return BindingBuilder.bind(jsonQueue()).to(exchange()).with(routingJsonKey);
    }

    /**
     * Configures the message converter to use JSON format for serialization and deserialization.
     *
     * @return A {@link MessageConverter} object configured to use Jackson for JSON processing.
     */
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configures the RabbitMQ template with the JSON message converter.
     *
     * @param connectionFactory The {@link ConnectionFactory} for RabbitMQ connections.
     * @return A configured {@link AmqpTemplate} object for sending and receiving messages.
     */
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}

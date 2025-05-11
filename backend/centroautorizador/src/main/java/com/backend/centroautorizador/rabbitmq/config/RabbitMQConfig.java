package com.backend.centroautorizador.rabbitmq.config;

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
 * Configuration class for RabbitMQ.
 * <p>
 * This class sets up the RabbitMQ components, including a queue, exchange, and binding.
 * It also configures the message converter and RabbitMQ template for message handling.
 * This configuration is conditionally enabled based on the property `rabbitmq.enabled`.
 * </p>
 */
@Configuration
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {

    /**
     * Name of the RabbitMQ queue, injected from application properties.
     */
    @Value("${rabbitmq.queue.name}")
    private String queue;

    /**
     * Name of the RabbitMQ exchange, injected from application properties.
     */
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    /**
     * Routing key for RabbitMQ messages, injected from application properties.
     */
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /**
     * Defines the RabbitMQ queue.
     *
     * @return The configured {@link Queue}.
     */
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    /**
     * Defines the RabbitMQ topic exchange.
     *
     * @return The configured {@link TopicExchange}.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    /**
     * Binds the queue to the exchange with the specified routing key.
     *
     * @return The configured {@link Binding}.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    /**
     * Configures the message converter to use JSON format.
     *
     * @return The configured {@link MessageConverter}.
     */
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configures the RabbitMQ template with the message converter.
     *
     * @param connectionFactory The {@link ConnectionFactory} for RabbitMQ connections.
     * @return The configured {@link AmqpTemplate}.
     */
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}

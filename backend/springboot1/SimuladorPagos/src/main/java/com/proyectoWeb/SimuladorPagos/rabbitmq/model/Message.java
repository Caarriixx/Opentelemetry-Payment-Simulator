package com.proyectoWeb.SimuladorPagos.rabbitmq.model;

/**
 * Represents a generic message to be sent to RabbitMQ.
 * <p>
 * This class encapsulates the content of a message that will be published to a RabbitMQ queue.
 * It provides a simple structure for text-based messages.
 */
public class Message {

    /**
     * Content of the message.
     */
    private String content;

    /**
     * Default constructor for creating an empty message.
     */
    public Message() {
    }

    /**
     * Constructor for creating a message with specific content.
     *
     * @param content The content of the message.
     */
    public Message(String content) {
        this.content = content;
    }

    /**
     * Gets the content of the message.
     *
     * @return The message content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     *
     * @param content The new content for the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns a string representation of the message object.
     *
     * @return A string in the format "Message{content='...'}".
     */
    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}

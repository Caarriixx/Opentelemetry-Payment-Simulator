package com.backend.centroautorizador.rabbitmq.model;

/**
 * Represents a simple message to be sent to RabbitMQ.
 * <p>
 * This class encapsulates the content of the message as a string. It can be
 * serialized and sent through RabbitMQ for various purposes.
 * </p>
 */
public class Message {

    /**
     * The content of the message.
     */
    private String content;

    /**
     * Default constructor.
     * <p>
     * Creates an empty {@link Message} object.
     * </p>
     */
    public Message() {
    }

    /**
     * Constructor with content.
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
     * @param content The new content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns a string representation of the message.
     *
     * @return A string representing the message content.
     */
    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}


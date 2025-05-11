package com.backend.centroautorizador.elasticsearch.model;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Represents a payment log stored in the Elasticsearch index "paylog".
 * <p>
 * This class encapsulates the details of a payment log, including the origin, destination,
 * amount, state, and a unique identifier. It is conditionally enabled based on the application
 * property `elasticsearch.enabled`.
 * </p>
 */
@Document(indexName = "paylog")
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true", matchIfMissing = true)
public class PayLog {

    /**
     * Unique identifier for the payment log entry.
     */
    @Id
    private String id;

    /**
     * Origin of the payment.
     */
    private String origin;

    /**
     * Destination of the payment.
     */
    private String destination;

    /**
     * Amount involved in the payment.
     */
    private int amount;

    /**
     * State of the payment (e.g., pending, completed, rejected).
     */
    private int state;

    /**
     * Constructor for creating a new {@link PayLog}.
     *
     * @param id          The unique identifier for the log.
     * @param origin      The origin of the payment.
     * @param destination The destination of the payment.
     * @param amount      The amount involved in the payment.
     * @param state       The state of the payment.
     */
    public PayLog(String id, String origin, String destination, int amount, int state) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.amount = amount;
        this.state = state;
    }

    /**
     * Gets the unique identifier of the payment log entry.
     *
     * @return The unique identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the payment log entry.
     *
     * @param id The new unique identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the origin of the payment.
     *
     * @return The origin of the payment.
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the origin of the payment.
     *
     * @param origin The new origin of the payment.
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Gets the destination of the payment.
     *
     * @return The destination of the payment.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination of the payment.
     *
     * @param destination The new destination of the payment.
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets the amount involved in the payment.
     *
     * @return The payment amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount involved in the payment.
     *
     * @param amount The new payment amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the state of the payment.
     *
     * @return The payment state.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the state of the payment.
     *
     * @param state The new payment state.
     */
    public void setState(int state) {
        this.state = state;
    }
}

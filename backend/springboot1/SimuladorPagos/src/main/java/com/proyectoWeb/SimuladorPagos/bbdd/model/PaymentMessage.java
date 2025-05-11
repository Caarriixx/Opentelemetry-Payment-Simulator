
package com.proyectoWeb.SimuladorPagos.bbdd.model;

import java.util.Date;

/**
 * Represents a payment message used for communication between services.
 * <p>
 * This class encapsulates details about a payment, including the sender, recipient,
 * amount, state, and date of the payment.
 */
public class PaymentMessage {

    /**
     * The user who initiated the payment.
     */
    private User sender;

    /**
     * The user who is the recipient of the payment.
     */
    private User receiver;

    /**
     * The amount of the payment.
     */
    private double amount;

    /**
     * The state of the payment (e.g., PENDING, COMPLETED).
     */
    private int state;

    /**
     * The date when the payment was initiated.
     */
    private Date date;

    /**
     * Default constructor.
     * <p>
     * Creates an empty {@link PaymentMessage} object.
     */
    public PaymentMessage() {
    }

    /**
     * Full constructor.
     * <p>
     * Initializes the {@link PaymentMessage} with the specified sender, recipient,
     * amount, state, and date.
     *
     * @param sender    The user who initiated the payment.
     * @param receiver The user who is the recipient of the payment.
     * @param amount    The amount of the payment.
     * @param state     The state of the payment.
     * @param date      The date when the payment was initiated.
     */
    public PaymentMessage(User sender, User receiver, double amount, int state, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.state = state;
        this.date = date;
    }

    /**
     * Gets the user who initiated the payment.
     *
     * @return The sender of the payment.
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the user who initiated the payment.
     *
     * @param sender The new sender of the payment.
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Gets the user who is the recipient of the payment.
     *
     * @return The recipient of the payment.
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Sets the user who is the recipient of the payment.
     *
     * @param receiver The new recipient of the payment.
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets the amount of the payment.
     *
     * @return The amount of the payment.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the payment.
     *
     * @param amount The new amount of the payment.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the state of the payment.
     *
     * @return The state of the payment.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the state of the payment.
     *
     * @param state The new state of the payment.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Gets the date when the payment was initiated.
     *
     * @return The date of the payment.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date when the payment was initiated.
     *
     * @param date The new date of the payment.
     */
    public void setDate(Date date) {
        this.date = date;
    }
}

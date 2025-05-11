/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyectoWeb.SimuladorPagos.bbdd.model;

import com.proyectoWeb.SimuladorPagos.bbdd.service.StateService;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Entity representing the payments table in the database.
 * <p>
 * This class models a payment transaction with details such as amount, date,
 * sender, receiver, and state. It includes lifecycle callbacks to set the `date`
 * field automatically when the entity is created.
 * </p>
 *
 * @author Rubén Bécares
 */
@Entity
@Table(name = "payments")
@NamedQueries({
        @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
        @NamedQuery(name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.id = :id"),
        @NamedQuery(name = "Payment.findByAmount", query = "SELECT p FROM Payment p WHERE p.amount = :amount"),
        @NamedQuery(name = "Payment.findByDate", query = "SELECT p FROM Payment p WHERE p.date = :date")
})
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the payment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /**
     * Amount of the payment.
     */
    @Column(name = "amount")
    private Double amount;

    /**
     * Date when the payment was made.
     */
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    /**
     * State of the payment (e.g., Pending, Completed).
     */
    @JoinColumn(name = "state", referencedColumnName = "state")
    @ManyToOne
    private State state;

    /**
     * The user who sent the payment.
     */
    @JoinColumn(name = "sender", referencedColumnName = "id")
    @ManyToOne
    private User sender;

    /**
     * The user who received the payment.
     */
    @JoinColumn(name = "receiver", referencedColumnName = "id")
    @ManyToOne
    private User receiver;

    /**
     * Sets the `date` field to the current timestamp on entity creation.
     */
    @PrePersist
    protected void onCreate() {
        this.date = Date.from(Instant.now());
    }

    /**
     * Default constructor.
     */
    public Payment() {
    }

    /**
     * Constructor with ID.
     *
     * @param id The unique identifier for the payment.
     */
    public Payment(Integer id) {
        this.id = id;
    }

    /**
     * Full constructor.
     *
     * @param id       The unique identifier for the payment.
     * @param amount   The amount of the payment.
     * @param date     The date when the payment was made.
     * @param state    The state of the payment.
     * @param sender   The user who sent the payment.
     * @param receiver The user who received the payment.
     */
    public Payment(Integer id, Double amount, Date date, State state, User sender, User receiver) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.state = state;
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Full constructor.
     *
     * @param amount   The amount of the payment.
     * @param date     The date when the payment was made.
     * @param state    The state of the payment.
     * @param sender   The user who sent the payment.
     * @param receiver The user who received the payment.
     */
    public Payment(Double amount, Date date, State state, User sender, User receiver) {
        this.id = -1;
        this.amount = amount;
        this.date = date;
        this.state = state;
        this.sender = sender;
        this.receiver = receiver;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    // Utility Methods

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", state=" + state +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}

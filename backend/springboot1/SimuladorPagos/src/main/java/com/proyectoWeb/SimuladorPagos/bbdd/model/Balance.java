/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyectoWeb.SimuladorPagos.bbdd.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Entity representing the balance table in the database.
 * <p>
 * This class models a balance with fields for its ID, value, and the last modified timestamp.
 * It includes lifecycle callbacks to automatically set the `modified` field on creation and update.
 * </p>
 *
 */
@Entity
@Table(name = "balances")
@NamedQueries({
        @NamedQuery(name = "Balance.findAll", query = "SELECT b FROM Balance b"),
        @NamedQuery(name = "Balance.findById", query = "SELECT b FROM Balance b WHERE b.id = :id"),
        @NamedQuery(name = "Balance.findByBalance", query = "SELECT b FROM Balance b WHERE b.balance = :balance"),
        @NamedQuery(name = "Balance.findByModified", query = "SELECT b FROM Balance b WHERE b.modified = :modified")
})
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the balance.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /**
     * Value of the balance.
     */
    @Column(name = "balance")
    private Double balance;

    /**
     * Timestamp of the last modification.
     */
    @Column(name = "modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    /**
     * Sets the `modified` field to the current timestamp on entity creation.
     */
    @PrePersist
    protected void onCreate() {
        this.modified = Date.from(Instant.now());
    }

    /**
     * Updates the `modified` field to the current timestamp on entity update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.modified = Date.from(Instant.now());
    }

    /**
     * Default constructor.
     */
    public Balance() {
    }

    /**
     * Constructor with ID.
     *
     * @param id The ID of the balance.
     */
    public Balance(Integer id) {
        this.id = id;
    }

    /**
     * Full constructor.
     *
     * @param id       The ID of the balance.
     * @param balance  The value of the balance.
     * @param modified The timestamp of the last modification.
     */
    public Balance(Integer id, Double balance, Date modified) {
        this.id = id;
        this.balance = balance;
        this.modified = modified;
    }

    // Getters and Setters

    /**
     * Gets the ID of the balance.
     *
     * @return The ID of the balance.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the balance.
     *
     * @param id The new ID of the balance.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the value of the balance.
     *
     * @return The value of the balance.
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance.
     *
     * @param balance The new value of the balance.
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * Gets the last modification timestamp.
     *
     * @return The timestamp of the last modification.
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Sets the last modification timestamp.
     *
     * @param modified The new modification timestamp.
     */
    public void setModified(Date modified) {
        this.modified = modified;
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
        if (!(object instanceof Balance)) {
            return false;
        }
        Balance other = (Balance) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", balance=" + balance +
                ", modified=" + modified +
                '}';
    }
}

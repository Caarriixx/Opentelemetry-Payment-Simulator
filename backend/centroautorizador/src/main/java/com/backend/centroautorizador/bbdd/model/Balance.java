package com.backend.centroautorizador.bbdd.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Entity representing the balances table in the database.
 * <p>
 * This class models a balance record, including fields for its ID, balance value,
 * and the last modification timestamp. Lifecycle callbacks are used to set the
 * `modified` field automatically on creation and update.
 * </p>
 *
 * @author Rubén Bécares
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
     * The monetary balance value.
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
     * Sets the `modified` field to the current timestamp when the entity is created.
     */
    @PrePersist
    protected void onCreate() {
        this.modified = Date.from(Instant.now());
    }

    /**
     * Updates the `modified` field to the current timestamp when the entity is updated.
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
     * @param id The unique identifier for the balance.
     */
    public Balance(Integer id) {
        this.id = id;
    }

    /**
     * Full constructor.
     *
     * @param id       The unique identifier for the balance.
     * @param balance  The monetary balance value.
     * @param modified The timestamp of the last modification.
     */
    public Balance(Integer id, Double balance, Date modified) {
        this.id = id;
        this.balance = balance;
        this.modified = modified;
    }

    /**
     * Gets the unique identifier of the balance.
     *
     * @return The ID of the balance.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the balance.
     *
     * @param id The new ID of the balance.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the monetary balance value.
     *
     * @return The balance value.
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Sets the monetary balance value.
     *
     * @param balance The new balance value.
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * Gets the timestamp of the last modification.
     *
     * @return The last modification timestamp.
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Sets the timestamp of the last modification.
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend.centroautorizador.bbdd.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Entity representing the states table in the database.
 * <p>
 * This class models a state with a unique identifier and a description.
 * It includes named queries for retrieving all states or searching by specific attributes.
 * </p>
 *
 * @author Rubén Bécares
 */
@Entity
@Table(name = "states")
@NamedQueries({
        @NamedQuery(name = "State.findAll", query = "SELECT s FROM State s"),
        @NamedQuery(name = "State.findByState", query = "SELECT s FROM State s WHERE s.state = :state"),
        @NamedQuery(name = "State.findByDescription", query = "SELECT s FROM State s WHERE s.description = :description")
})
public class State implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the state.
     */
    @Id
    @Basic(optional = false)
    @Column(name = "state")
    private Integer state;

    /**
     * Description of the state.
     */
    @Column(name = "description")
    private String description;

    /**
     * Default constructor.
     */
    public State() {
    }

    /**
     * Constructor with state ID.
     *
     * @param state The unique identifier for the state.
     */
    public State(Integer state) {
        this.state = state;
    }

    /**
     * Full constructor.
     *
     * @param state       The unique identifier for the state.
     * @param description A description of the state.
     */
    public State(Integer state, String description) {
        this.state = state;
        this.description = description;
    }

    /**
     * Gets the unique identifier of the state.
     *
     * @return The state ID.
     */
    public Integer getState() {
        return state;
    }

    /**
     * Sets the unique identifier of the state.
     *
     * @param state The new state ID.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * Gets the description of the state.
     *
     * @return The state description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the state.
     *
     * @param description The new description of the state.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // Utility Methods

    /**
     * Generates a hash code for the state.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (state != null ? state.hashCode() : 0);
        return hash;
    }

    /**
     * Compares this state to another object for equality.
     *
     * @param object The object to compare to.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof State)) {
            return false;
        }
        State other = (State) object;
        return (this.state != null || other.state == null) && (this.state == null || this.state.equals(other.state));
    }

    /**
     * Returns a string representation of the state.
     *
     * @return A string containing the state ID and description.
     */
    @Override
    public String toString() {
        return "State{" +
                "state=" + state +
                ", description='" + description + '\'' +
                '}';
    }
}

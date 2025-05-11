/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyectoWeb.SimuladorPagos.bbdd.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Entity representing the states table in the database.
 * <p>
 * This class models a state with an identifier and a description.
 * It includes queries for retrieving all states or searching by specific attributes.
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

    // Getters and Setters

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (state != null ? state.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof State)) {
            return false;
        }
        State other = (State) object;
        return (this.state != null || other.state == null) && (this.state == null || this.state.equals(other.state));
    }

    @Override
    public String toString() {
        return "State{" +
                "state=" + state +
                ", description='" + description + '\'' +
                '}';
    }
}

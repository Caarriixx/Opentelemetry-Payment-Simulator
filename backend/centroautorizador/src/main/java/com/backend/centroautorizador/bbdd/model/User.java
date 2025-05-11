/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend.centroautorizador.bbdd.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Entity representing the users table in the database.
 * <p>
 * This class models a user with fields for email, password, name, surname, associated balance,
 * and other relevant details. Lifecycle callbacks are used to set the `createdAt` field
 * automatically when the entity is created.
 * </p>
 *
 * @author Rubén Bécares
 */
@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
        @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
        @NamedQuery(name = "User.findByCc", query = "SELECT u FROM User u WHERE u.cc = :cc"),
        @NamedQuery(name = "User.findByDni", query = "SELECT u FROM User u WHERE u.dni = :dni"),
        @NamedQuery(name = "User.findByCreatedAt", query = "SELECT u FROM User u WHERE u.createdAt = :createdAt")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /**
     * Email address of the user.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * Encrypted password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * First name of the user.
     */
    @Column(name = "name")
    private String name;

    /**
     * Last name of the user.
     */
    @Column(name = "surname")
    private String surname;

    /**
     * Associated balance of the user.
     */
    @JoinColumn(name = "cc", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.PERSIST)
    private Balance cc;

    /**
     * National ID (DNI) of the user.
     */
    @Column(name = "dni")
    private String dni;

    /**
     * Timestamp indicating when the user was created.
     */
    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * Sets the `createdAt` field to the current timestamp when the entity is created.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = Date.from(Instant.now());
    }

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructor with ID.
     *
     * @param id The unique identifier for the user.
     */
    public User(Integer id) {
        this.id = id;
    }

    /**
     * Full constructor.
     *
     * @param id        The unique identifier for the user.
     * @param email     The email address of the user.
     * @param password  The password of the user.
     * @param name      The first name of the user.
     * @param surname   The last name of the user.
     * @param cc        The associated balance of the user.
     * @param dni       The national ID of the user.
     * @param createdAt The creation timestamp.
     */
    public User(Integer id, String email, String password, String name, String surname, Balance cc, String dni, Date createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.cc = cc;
        this.dni = dni;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Balance getCc() {
        return cc;
    }

    public void setCc(Balance cc) {
        this.cc = cc;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cc=" + cc +
                ", dni='" + dni + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

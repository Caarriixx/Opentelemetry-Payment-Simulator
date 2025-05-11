/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyectoWeb.SimuladorPagos.bbdd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Entity representing the users table in the database.
 * <p>
 * This class models a user with fields for email, password, name, surname, associated balance,
 * and other relevant details. It includes lifecycle callbacks to set the `createdAt` field
 * automatically when the entity is created.
 * </p>
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
    @NotNull(message = "Email cannot be null.")
    @Email(message = "Email must be valid.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Encrypted password of the user.
     */
    @NotNull(message = "Password cannot be null.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * First name of the user.
     */
    @Size(max = 50, message = "Name cannot exceed 50 characters.")
    @Column(name = "name")
    private String name;

    /**
     * Last name of the user.
     */
    @Size(max = 50, message = "Surname cannot exceed 50 characters.")
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
    @Size(max = 15, message = "DNI cannot exceed 15 characters.")
    @Column(name = "dni")
    private String dni;

    /**
     * Timestamp indicating when the user was created.
     */
    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * Sets the `createdAt` field to the current timestamp on entity creation.
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

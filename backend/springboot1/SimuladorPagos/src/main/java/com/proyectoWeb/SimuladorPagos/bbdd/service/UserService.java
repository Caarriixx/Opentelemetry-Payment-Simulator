package com.proyectoWeb.SimuladorPagos.bbdd.service;


import com.proyectoWeb.SimuladorPagos.bbdd.model.Balance;
import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import com.proyectoWeb.SimuladorPagos.bbdd.repository.UserRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing user-related operations.
 * Provides methods for registering, searching, and listing users,
 * as well as securely managing their data.
 */
@Service
public class UserService {

    /**
     * Repository for interacting with the user database.
     */
    private final UserRepository userRepository;

    private final Tracer tracer;

    /**
     * Encoder for encrypting user passwords.
     */
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService.
     *
     * @param userRepository the injected user repository.
     */
    @Autowired
    public UserService(UserRepository userRepository, Tracer tracer) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.tracer = tracer;
    }

    /**
     * Inserts a user into the database.
     *
     * @param user the user to be saved.
     */
    public void insertUser(User user) {
        userRepository.save(user);
    }

    /**
     * Returns a list of all users stored in the database.
     *
     * @return an iterable containing all users.
     */
    public Iterable<User> listUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     *
     * @param user the ID of the user to search for.
     * @return an Optional containing the user if found, or empty if not.
     */
    public Optional<User> findByID(Integer user) {
        return userRepository.findById(user);
    }

    /**
     * Finds a user by their name.
     *
     * @param user the name of the user to search for.
     * @return an Optional containing the user if found, or empty if not.
     */
    public Optional<User> findByName(String user) {
        return userRepository.findByName(user);
    }

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to search for.
     * @return an Optional containing the user if found, or empty if not.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Registers a new user in the database.
     *
     * @param user the user to be registered.
     * @return the registered user.
     * @throws IllegalArgumentException if the email is already registered or mandatory data is missing.
     */
    public User registerUser(User user) {
        Span span = tracer.spanBuilder("register-user").startSpan();

        try {
            span.setAttribute("user.email", user.getEmail());
            span.setAttribute("user.dni", user.getDni());

            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                span.setStatus(StatusCode.ERROR, "Email already registered");
                throw new IllegalArgumentException("Email is already registered.");
            }

            if (user.getEmail() == null || user.getPassword() == null) {
                span.setStatus(StatusCode.ERROR, "Missing data");
                throw new IllegalArgumentException("Email and password are mandatory.");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.getCc() == null) {
                user.setCc(new Balance());
            }

            span.setStatus(StatusCode.OK);
            return userRepository.save(user);

        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "Exception in registerUser");
            throw e;
        } finally {
            span.end();
        }
    }


}

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

            // Validación de email
            if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().endsWith(".com")) {
                span.setStatus(StatusCode.ERROR, "Invalid email format");
                span.setAttribute("user.registration_error", "invalid_email");
                throw new IllegalArgumentException("Email must contain '@' and end with '.com'");
            }

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                span.setStatus(StatusCode.ERROR, "Email already registered");
                span.setAttribute("user.registration_error", "email_exists");
                throw new IllegalArgumentException("Email is already registered.");
            }

            // Validación de contraseña
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                span.setStatus(StatusCode.ERROR, "Password is missing");
                span.setAttribute("user.registration_error", "missing_password");
                throw new IllegalArgumentException("Password is mandatory.");
            }

            // Validación de DNI
            if (user.getDni() == null || !user.getDni().matches("\\d{8}[A-Z]")) {
                span.setStatus(StatusCode.ERROR, "Invalid DNI format");
                span.setAttribute("user.registration_error", "invalid_dni");
                throw new IllegalArgumentException("DNI must have 8 digits followed by an uppercase letter.");
            }

            // Validación de ID si se asigna manualmente (por seguridad)
            if (user.getId() != null && (user.getId() <= 0 || userRepository.findById(user.getId()).isPresent())) {
                span.setStatus(StatusCode.ERROR, "Invalid or duplicate ID");
                span.setAttribute("user.registration_error", "invalid_or_duplicate_id");
                throw new IllegalArgumentException("User ID must be a positive integer and not already exist.");
            }

            // Cifrado de contraseña
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Asignación de balance si falta
            if (user.getCc() == null) {
                user.setCc(new Balance());
            }

            // Validación de saldo positivo
            if (user.getCc().getBalance() < 0) {
                span.setStatus(StatusCode.ERROR, "Negative balance");
                span.setAttribute("user.registration_error", "negative_balance");
                throw new IllegalArgumentException("Initial balance cannot be negative.");
            }

            // Atributos adicionales de trazabilidad
            span.setAttribute("user.name", user.getName());
            span.setAttribute("user.surname", user.getSurname());
            span.setAttribute("user.balance.initial", user.getCc().getBalance());

            User savedUser = userRepository.save(user);
            span.setStatus(StatusCode.OK, "User successfully registered");
            span.setAttribute("user.registration_result", "success");
            return savedUser;

        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "Exception in registerUser");
            span.setAttribute("user.registration_result", "error");
            throw e;
        } finally {
            span.end();
        }
    }


}

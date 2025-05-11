package com.proyectoWeb.SimuladorPagos.bbdd.repository;

import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their name.
     *
     * @param name the name of the user to search for.
     * @return an Optional containing the user if found, or empty if not.
     */
    Optional<User> findByName(String name);

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to search for.
     * @return an Optional containing the user if found, or empty if not.
     */
    Optional<User> findByEmail(String email);
}

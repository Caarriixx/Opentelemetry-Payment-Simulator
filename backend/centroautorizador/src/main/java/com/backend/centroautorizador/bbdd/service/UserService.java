package com.backend.centroautorizador.bbdd.service;

import com.backend.centroautorizador.bbdd.model.User;
import com.backend.centroautorizador.bbdd.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing users.
 * <p>
 * This class provides methods to interact with the {@link UserRepository},
 * including inserting users, listing all users, and finding users by their ID.
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor for {@link UserService}.
     *
     * @param userRepository The repository to interact with {@link User} records.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user The {@link User} entity to be saved.
     */
    public void insertUser(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return An {@link Iterable} of {@link User} entities.
     */
    public Iterable<User> listUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their unique ID.
     *
     * @param user The ID of the user to be retrieved.
     * @return An {@link Optional} containing the {@link User} entity if found, or empty if not.
     */
    public Optional<User> findByID(Integer user) {
        return userRepository.findById(user);
    }
}


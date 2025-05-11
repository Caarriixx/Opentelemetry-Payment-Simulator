package com.backend.centroautorizador.bbdd.service;

import com.backend.centroautorizador.bbdd.model.State;
import com.backend.centroautorizador.bbdd.repo.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing states.
 * <p>
 * This class provides methods to interact with the {@link StateRepository},
 * including inserting states, listing all states, and finding states by their ID.
 * </p>
 */
@Service
public class StateService {

    private final StateRepository stateRepository;

    /**
     * Constructor for {@link StateService}.
     *
     * @param stateRepository The repository to interact with {@link State} records.
     */
    @Autowired
    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    /**
     * Inserts a new state into the database.
     *
     * @param state The {@link State} entity to be saved.
     */
    public void insertState(State state) {
        stateRepository.save(state);
    }

    /**
     * Retrieves all states from the database.
     *
     * @return An {@link Iterable} of {@link State} entities.
     */
    public Iterable<State> listStates() {
        return stateRepository.findAll();
    }

    /**
     * Finds a state by its unique ID.
     *
     * @param state The ID of the state to be retrieved.
     * @return An {@link Optional} containing the {@link State} entity if found, or empty if not.
     */
    public Optional<State> findByState(Integer state) {
        return stateRepository.findById(state);
    }
}


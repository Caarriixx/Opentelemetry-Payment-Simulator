package com.backend.centroautorizador.bbdd.contoller;

import com.backend.centroautorizador.bbdd.model.State;
import com.backend.centroautorizador.bbdd.repo.StateRepository;
import com.backend.centroautorizador.bbdd.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * REST Controller for managing states.
 * <p>
 * This controller provides endpoints to add new states and retrieve all existing states.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "StateController", description = "Endpoints for managing states.")
public class StateController {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateService stateService;

    /**
     * Adds a new state to the system.
     *
     * @param state The state to be added.
     * @return A success message indicating that the state was saved.
     */
    @Operation(summary = "Add a new state", description = "Inserts a new state into the database.")
    @PostMapping("/state")
    public ResponseEntity<String> addNewState(@RequestBody State state) {
        stateService.insertState(state);
        return ResponseEntity.ok("New State saved");
    }

    /**
     * Retrieves all states from the system.
     *
     * @return An iterable containing all states.
     */
    @Operation(summary = "Get all states", description = "Fetches all states from the database.")
    @GetMapping("/state")
    public Iterable<State> findAll() {
        return stateService.listStates();
    }
}

package com.backend.centroautorizador.bbdd.repo;

import com.backend.centroautorizador.bbdd.model.Payment;
import com.backend.centroautorizador.bbdd.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Payment} entities.
 * <p>
 * This interface provides CRUD operations and query methods for interacting
 * with the `payments` table in the database. It extends {@link JpaRepository},
 * which includes methods such as save, findById, findAll, and delete.
 * </p>
 */
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    /**
     * Finds all payments with the specified state.
     *
     * @param state The {@link State} to filter payments by.
     * @return A list of {@link Payment} entities that match the given state.
     */
    List<Payment> findByState(Optional<State> state);
}


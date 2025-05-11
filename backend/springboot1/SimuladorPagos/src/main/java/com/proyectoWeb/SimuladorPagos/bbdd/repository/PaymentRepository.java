package com.proyectoWeb.SimuladorPagos.bbdd.repository;

import com.proyectoWeb.SimuladorPagos.bbdd.model.Payment;
import com.proyectoWeb.SimuladorPagos.bbdd.model.State;
import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Payment entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    /**
     * Finds a list of payments by their state.
     *
     * @param state the state to filter payments by, wrapped in an Optional.
     * @return a list of payments matching the given state.
     */
    List<Payment> findByState(Optional<State> state);

    Collection<Payment> findByReceiver(User receiver);

    Collection<? extends Payment> findBySender(User sender);
}

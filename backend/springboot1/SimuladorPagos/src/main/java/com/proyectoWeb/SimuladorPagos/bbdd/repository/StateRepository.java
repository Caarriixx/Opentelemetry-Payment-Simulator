package com.proyectoWeb.SimuladorPagos.bbdd.repository;

import com.proyectoWeb.SimuladorPagos.bbdd.model.State;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for managing State entities.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface StateRepository extends JpaRepository<State, Integer> {
}

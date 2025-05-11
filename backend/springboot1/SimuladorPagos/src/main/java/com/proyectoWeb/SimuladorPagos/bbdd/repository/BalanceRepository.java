package com.proyectoWeb.SimuladorPagos.bbdd.repository;

import com.proyectoWeb.SimuladorPagos.bbdd.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Balance entities.
 * Extends JpaRepository to provide CRUD operations and query methods.
 */
public interface BalanceRepository extends JpaRepository<Balance, Integer> {
}


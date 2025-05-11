package com.backend.centroautorizador.bbdd.repo;

import com.backend.centroautorizador.bbdd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface provides CRUD operations and query methods for interacting
 * with the `users` table in the database. It extends {@link JpaRepository},
 * which includes methods such as save, findById, findAll, and delete.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}


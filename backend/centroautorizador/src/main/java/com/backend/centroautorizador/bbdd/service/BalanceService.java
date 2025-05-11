package com.backend.centroautorizador.bbdd.service;

import com.backend.centroautorizador.bbdd.model.Balance;
import com.backend.centroautorizador.bbdd.repo.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing balances.
 * <p>
 * This class provides methods to interact with the {@link BalanceRepository},
 * including inserting, listing, and finding balances by ID.
 * </p>
 */
@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    /**
     * Constructor for {@link BalanceService}.
     *
     * @param balanceRepository The {@link BalanceRepository} to interact with balances.
     */
    @Autowired
    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     * Inserts a new balance into the database.
     *
     * @param balance The {@link Balance} entity to be saved.
     */
    public void insertBalance(Balance balance) {
        balanceRepository.save(balance);
    }

    /**
     * Retrieves all balances from the database.
     *
     * @return An {@link Iterable} of {@link Balance} entities.
     */
    public Iterable<Balance> listBalances() {
        return balanceRepository.findAll();
    }

    /**
     * Finds a balance by its unique ID.
     *
     * @param balance The ID of the balance to be retrieved.
     * @return An {@link Optional} containing the {@link Balance} entity if found, or empty if not.
     */
    public Optional<Balance> findById(Integer balance) {
        return balanceRepository.findById(balance);
    }
}


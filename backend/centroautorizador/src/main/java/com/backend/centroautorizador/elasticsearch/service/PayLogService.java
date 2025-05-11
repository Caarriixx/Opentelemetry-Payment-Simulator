package com.backend.centroautorizador.elasticsearch.service;

import com.backend.centroautorizador.elasticsearch.model.PayLog;
import com.backend.centroautorizador.elasticsearch.repo.PayLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Service for managing {@link PayLog} entries in Elasticsearch.
 * <p>
 * This service provides methods to retrieve, insert, update, and delete payment log entries
 * in the Elasticsearch index `paylog`. It is conditionally enabled based on the application
 * property `elasticsearch.enabled`.
 * </p>
 */
@Service
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true", matchIfMissing = true)
public class PayLogService {

    @Autowired
    private PayLogRepo payLogRepo;

    /**
     * Retrieves all payment logs from Elasticsearch.
     *
     * @return An {@link Iterable} of {@link PayLog} entries.
     */
    public Iterable<PayLog> getPayLogs() {
        return payLogRepo.findAll();
    }

    /**
     * Inserts a new payment log into Elasticsearch.
     *
     * @param payLog The {@link PayLog} to be inserted.
     * @return The saved {@link PayLog} entry.
     */
    public PayLog insertPayLog(PayLog payLog) {
        return payLogRepo.save(payLog);
    }

    /**
     * Updates an existing payment log in Elasticsearch.
     *
     * @param payLog The updated {@link PayLog} details.
     * @param id     The unique identifier of the payment log to update.
     * @return The updated {@link PayLog} entry.
     * @throws java.util.NoSuchElementException If the log with the given ID does not exist.
     */
    public PayLog updatePayLog(PayLog payLog, String id) {
        PayLog existingPayLog = payLogRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Payment log with ID " + id + " not found."));
        existingPayLog.setDestination(payLog.getDestination());
        existingPayLog.setOrigin(payLog.getOrigin());
        existingPayLog.setAmount(payLog.getAmount());
        existingPayLog.setState(payLog.getState());
        return payLogRepo.save(existingPayLog);
    }

    /**
     * Deletes a payment log from Elasticsearch by its unique ID.
     *
     * @param id The unique identifier of the payment log to delete.
     */
    public void deletePayLog(String id) {
        payLogRepo.deleteById(id);
    }
}

package com.proyectoWeb.SimuladorPagos.elastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyectoWeb.SimuladorPagos.elastic.repo.ErrorLogRepository;
import com.proyectoWeb.SimuladorPagos.elastic.entity.ErrorLog;

import java.time.Instant;

/**
 * Service for managing error logs in Elasticsearch.
 * <p>
 * This service provides methods to log errors and retrieve all error logs stored in the Elasticsearch index.
 */
@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    /**
     * Logs an error in Elasticsearch.
     * <p>
     * Creates a new {@link ErrorLog} with the provided details and saves it to the Elasticsearch index.
     *
     * @param type    The type of the error (e.g., "SYSTEM", "VALIDATION").
     * @param message A descriptive message explaining the error.
     * @param details Additional details about the error, such as a stack trace or context information.
     */
    public void logError(String type, String message, String details) {
        ErrorLog errorLog = new ErrorLog(null, type, message, Instant.now(), details);
        errorLogRepository.save(errorLog);
    }

    /**
     * Retrieves all error logs stored in Elasticsearch.
     *
     * @return An {@link Iterable} containing all {@link ErrorLog} documents stored in the Elasticsearch index.
     */
    public Iterable<ErrorLog> getAllErrors() {
        return errorLogRepository.findAll();
    }
}



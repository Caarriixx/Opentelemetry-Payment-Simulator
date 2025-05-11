package com.backend.centroautorizador.elasticsearch.controller;

import com.backend.centroautorizador.elasticsearch.model.PayLog;
import com.backend.centroautorizador.elasticsearch.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * REST Controller for managing PayLog entries in Elasticsearch.
 * <p>
 * This controller provides endpoints to retrieve all logs and insert new logs.
 * It is enabled only if Elasticsearch is configured.
 */
@RestController
@RequestMapping("/api/v1/log")
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "PayLogController", description = "Endpoints for managing payment logs in Elasticsearch.")
public class PayLogController {

    private final PayLogService payLogService;

    /**
     * Constructor for {@link PayLogController}.
     *
     * @param payLogService The service managing {@link PayLog} operations.
     */
    @Autowired
    public PayLogController(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    /**
     * Retrieves all payment logs from Elasticsearch.
     *
     * @return An iterable collection of {@link PayLog} entries.
     */
    @Operation(summary = "Retrieve all payment logs", description = "Fetches all payment log entries from Elasticsearch.")
    @GetMapping("/findAll")
    public Iterable<PayLog> findAll() {
        return payLogService.getPayLogs();
    }

    /**
     * Inserts a new payment log into Elasticsearch.
     *
     * @param payLog The {@link PayLog} entry to be saved.
     * @return The saved {@link PayLog} entry.
     */
    @Operation(summary = "Insert a new payment log", description = "Creates and saves a new payment log entry in Elasticsearch.")
    @PostMapping("/insert")
    public PayLog insert(@RequestBody PayLog payLog) {
        return payLogService.insertPayLog(payLog);
    }
}


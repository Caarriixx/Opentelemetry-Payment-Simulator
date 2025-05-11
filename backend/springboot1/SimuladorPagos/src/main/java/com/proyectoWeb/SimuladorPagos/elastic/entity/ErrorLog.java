package com.proyectoWeb.SimuladorPagos.elastic.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;

/**
 * Represents an error log stored in Elasticsearch.
 * <p>
 * This class is mapped to the Elasticsearch index "errors" and is used to log and retrieve
 * information about system or validation errors.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "errors")
public class ErrorLog {

    /**
     * Unique identifier for the error log.
     */
    @Id
    private String id;

    /**
     * Type of error (e.g., VALIDATION, SYSTEM).
     * <p>
     * Indicates the nature of the error, such as validation issues or system failures.
     */
    private String type;

    /**
     * Descriptive message of the error.
     * <p>
     * Provides a human-readable explanation of the error.
     */
    private String message;

    /**
     * Timestamp when the error occurred.
     * <p>
     * Represents the exact date and time of the error occurrence.
     */
    private Instant timestamp;

    /**
     * Additional details about the error, such as stack trace or context.
     * <p>
     * This field can include information like the origin of the error or debugging data.
     */
    private String details;
}

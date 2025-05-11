package com.backend.centroautorizador.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Represents an Elasticsearch index.
 * <p>
 * This class is a generic representation of an Elasticsearch index with a unique identifier.
 * The actual index name is defined in the application properties using the key `elasticsearch.index.name`.
 * </p>
 */
@Document(indexName = "${elasticsearch.index.name}")
public class Index {

    /**
     * Unique identifier for the index entry.
     */
    @Id
    private String id;

    /**
     * Default constructor.
     * <p>
     * Creates an empty {@link Index} object.
     */
    public Index() {
    }

    /**
     * Constructor with ID.
     *
     * @param id The unique identifier for the index entry.
     */
    public Index(String id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier of the index entry.
     *
     * @return The ID of the index entry.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the index entry.
     *
     * @param id The new ID for the index entry.
     */
    public void setId(String id) {
        this.id = id;
    }
}


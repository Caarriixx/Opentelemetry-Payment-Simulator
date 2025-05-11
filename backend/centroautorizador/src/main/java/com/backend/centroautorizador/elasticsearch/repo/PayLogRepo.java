package com.backend.centroautorizador.elasticsearch.repo;

import com.backend.centroautorizador.elasticsearch.model.PayLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing {@link PayLog} entries in Elasticsearch.
 * <p>
 * This repository provides CRUD operations and query methods for interacting
 * with the `paylog` index in Elasticsearch. It is conditionally enabled based
 * on the application property `elasticsearch.enabled`.
 * </p>
 */
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true", matchIfMissing = true)
@Repository
public interface PayLogRepo extends ElasticsearchRepository<PayLog, String> {
}


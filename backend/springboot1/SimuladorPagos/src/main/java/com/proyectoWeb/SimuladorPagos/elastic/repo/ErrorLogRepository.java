package com.proyectoWeb.SimuladorPagos.elastic.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.proyectoWeb.SimuladorPagos.elastic.entity.ErrorLog;

/**
 * Repository for managing {@link ErrorLog} documents in Elasticsearch.
 * <p>
 * This interface extends {@link ElasticsearchRepository} to provide CRUD operations
 * and additional query methods for the {@link ErrorLog} entity.
 * It is mapped to the Elasticsearch index defined in the {@link ErrorLog} class.
 */
public interface ErrorLogRepository extends ElasticsearchRepository<ErrorLog, String> {
    // Custom query methods can be added here if needed
}



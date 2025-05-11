package com.proyectoWeb.SimuladorPagos.elastic.repo;

import com.proyectoWeb.SimuladorPagos.elastic.entity.Paymente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Repository for managing Payment documents in Elasticsearch.
 * <p>
 * This interface extends {@link ElasticsearchRepository} to provide basic CRUD operations for the {@link Paymente} entity.
 * Additionally, it includes custom query methods to find payments by sender and receiver.
 */
public interface Paymentrepository extends ElasticsearchRepository<Paymente, Integer> {

    /**
     * Finds all payments sent by a specific sender.
     *
     * @param sender The ID of the sender whose payments are to be retrieved.
     * @return A list of {@link Paymente} objects representing payments sent by the specified sender.
     */
    List<Paymente> findBySender(Integer sender);

    /**
     * Finds all payments received by a specific receiver.
     *
     * @param receiver The ID of the receiver whose payments are to be retrieved.
     * @return A list of {@link Paymente} objects representing payments received by the specified receiver.
     */
    List<Paymente> findByReceiver(Integer receiver);
}




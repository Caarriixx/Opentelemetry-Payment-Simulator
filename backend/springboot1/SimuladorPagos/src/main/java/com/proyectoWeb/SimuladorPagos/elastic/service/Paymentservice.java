package com.proyectoWeb.SimuladorPagos.elastic.service;

import com.proyectoWeb.SimuladorPagos.elastic.entity.Paymente;
import com.proyectoWeb.SimuladorPagos.elastic.repo.Paymentrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing payments in Elasticsearch.
 * <p>
 * This service provides methods to interact with the payment index in Elasticsearch,
 * including saving new payment documents and querying payments by sender or receiver.
 */
@Service
public class Paymentservice {

    @Autowired
    private Paymentrepository paymentRepository;

    /**
     * Saves a new payment document in Elasticsearch.
     *
     * @param payment The {@link Paymente} object to be stored in Elasticsearch.
     * @return The saved {@link Paymente} object with its generated ID.
     */
    public Paymente savePayment(Paymente payment) {
        Paymente result = paymentRepository.save(payment);
        System.out.println("✅ Documento guardado en Elasticsearch con ID: " + result.getId());
        return result;
    }

    /**
     * Finds all payments sent by a specific user.
     *
     * @param sender The ID of the sender whose payments are to be retrieved.
     * @return A list of {@link Paymente} objects representing payments sent by the specified sender.
     */
    public List<Paymente> findPaymentsBySender(Integer sender) {
        return paymentRepository.findBySender(sender);
    }

    /**
     * Finds all payments received by a specific user.
     *
     * @param receiver The ID of the receiver whose payments are to be retrieved.
     * @return A list of {@link Paymente} objects representing payments received by the specified receiver.
     */
    public List<Paymente> findPaymentsByReceiver(Integer receiver) {
        return paymentRepository.findByReceiver(receiver);
    }
}

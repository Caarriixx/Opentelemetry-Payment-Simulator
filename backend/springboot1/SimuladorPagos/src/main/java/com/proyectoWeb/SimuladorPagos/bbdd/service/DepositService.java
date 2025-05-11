package com.proyectoWeb.SimuladorPagos.bbdd.service;

import com.proyectoWeb.SimuladorPagos.bbdd.model.Payment;
import com.proyectoWeb.SimuladorPagos.bbdd.model.PaymentMessage;
import com.proyectoWeb.SimuladorPagos.bbdd.model.State;
import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import com.proyectoWeb.SimuladorPagos.bbdd.repository.PaymentRepository;
//import com.backend.centroautorizador.rabbitmq.publisher.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DepositService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepositService.class);

    private PaymentRepository paymentRepository;

    private StateService stateService;


    @Autowired
    public DepositService(PaymentRepository paymentRepository, StateService stateService) {
        this.paymentRepository = paymentRepository;
        this.stateService = stateService;
    }

    public Iterable<Payment> getDeposits(User user){
        Collection<Payment> aux = paymentRepository.findByReceiver(user);
        aux.addAll(paymentRepository.findBySender(user));
        return aux;
    }

    @Scheduled(fixedRate = 10000)
    public void checkForNewInserts() {
        Collection<Payment> newRecords = paymentRepository.findByState(stateService.findByState(2));

        if (!newRecords.isEmpty()) {
            for (Payment record : newRecords) {
                // Procesa el registro nuevo
                LOGGER.info(String.format("Procesando nuevo registro ->  %s", record));
//                System.out.println("Procesando nuevo registro con ID: " + record.getId());

                // Aquí pones la lógica que quieras al detectar la inserción

                // Marcar como procesado
                Optional<State> state = stateService.findByState(4);
                record.setState(state.orElse(record.getState()));
                paymentRepository.save(record);
                LOGGER.info(String.format("Registro procesado ->  %s", record));
            }
        }
    }
}

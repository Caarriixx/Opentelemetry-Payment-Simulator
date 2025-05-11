package com.proyectoWeb.SimuladorPagos.bbdd.service;

import com.proyectoWeb.SimuladorPagos.bbdd.model.State;
import com.proyectoWeb.SimuladorPagos.bbdd.repository.StateRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StateService {

    private StateRepository stateRepository;

    //añadi
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }


    public Iterable<State> listStates(){
        return stateRepository.findAll();
    }

    //public Optional<State> findByState(Integer state){
     //   return stateRepository.findById(state);
    //}

    public Optional<State> findByState(Integer state){
        TypedQuery<State> query = entityManager.createNamedQuery("State.findByState", State.class);
        query.setParameter("state", state);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}

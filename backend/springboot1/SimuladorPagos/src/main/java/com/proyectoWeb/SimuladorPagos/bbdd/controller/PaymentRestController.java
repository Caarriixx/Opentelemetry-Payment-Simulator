package com.proyectoWeb.SimuladorPagos.bbdd.controller;

import com.proyectoWeb.SimuladorPagos.bbdd.model.PaymentMessage;
import com.proyectoWeb.SimuladorPagos.bbdd.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for inserting payments via HTTP POST for testing purposes.
 */
@RestController
@RequestMapping("/api")
public class PaymentRestController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentRestController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public String createPayment(@RequestBody PaymentMessage paymentMessage) {
        System.out.println("Received payment via REST: " + paymentMessage);
        paymentService.insertPayment(paymentMessage);
        return "Payment received and inserted!";
    }
}

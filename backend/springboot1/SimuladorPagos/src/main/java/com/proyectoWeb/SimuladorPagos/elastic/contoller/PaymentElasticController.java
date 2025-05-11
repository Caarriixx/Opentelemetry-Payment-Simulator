package com.proyectoWeb.SimuladorPagos.elastic.contoller;

import com.proyectoWeb.SimuladorPagos.elastic.entity.Paymente;
import com.proyectoWeb.SimuladorPagos.elastic.service.Paymentservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentElasticController {

    @Autowired
    private Paymentservice paymentService;

    @Operation(summary = "Save a payment", description = "Creates a new payment record in the system.")
    @PostMapping("/save")
    public ResponseEntity<Paymente> savePayment(
            @Parameter(description = "Payment object to be saved") @RequestBody Paymente payment) {
        return new ResponseEntity<>(paymentService.savePayment(payment), HttpStatus.CREATED);
    }

    @Operation(summary = "Get payments by sender ID", description = "Fetches payments made by a specific sender.")
    @GetMapping("/sender/{id}")
    public ResponseEntity<List<Paymente>> getPaymentsBySender(
            @Parameter(description = "ID of the sender", example = "123") @PathVariable Integer id) {
        List<Paymente> payments = paymentService.findPaymentsBySender(id);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
}


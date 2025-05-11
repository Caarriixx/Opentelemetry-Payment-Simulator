package com.proyectoWeb.SimuladorPagos.bbdd.controller;

import com.proyectoWeb.SimuladorPagos.bbdd.model.Payment;
import com.proyectoWeb.SimuladorPagos.bbdd.model.PaymentMessage;
import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import com.proyectoWeb.SimuladorPagos.bbdd.service.DepositService;
import com.proyectoWeb.SimuladorPagos.bbdd.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @MessageMapping("/payment/{userId}")
    @SendTo("/topic/payment/{userId}")
    public PaymentMessage handlePayment(@DestinationVariable String userId, PaymentMessage message) {
        // Lógica de manejo de pago, por ejemplo, actualización del estado o registro en base de datos
        System.out.println("Payment received from: " + message.getSender() + " to " + message.getReceiver() +
                " Amount: " + message.getAmount() + " State: " + message.getState() + " Date: " + message.getDate());
        this.paymentService.insertPayment(message);
        return new PaymentMessage(message.getSender(), message.getReceiver(), message.getAmount(), message.getState(), message.getDate());
    }

    @SendTo("/topic/payment/notification/{userId}")
    public static Payment handlePayment(@DestinationVariable String userId, Payment payment) {
        // Lógica de manejo de pago, por ejemplo, actualización del estado o registro en base de datos
        //userId = payment.getSender().getId().toString();
        System.out.println("Payment notification");
        return payment;
    }

    @MessageMapping("/payment/user/{userId}")
    @SendTo("/topic/payment/user/{userId}")
    public Iterable<Payment> getPayments(@DestinationVariable String userId, User user) {
        // Lógica de manejo de pago, por ejemplo, actualización del estado o registro en base de datos
        System.out.println("List payments");
        Iterable<Payment> payments = paymentService.getPayments(user);
        System.out.println(payments);
        return payments;
    }

    @Autowired
    DepositService depositService;

    @MessageMapping("/deposit/{userId}")
    @SendTo("/topic/deposit/{userId}")
    public PaymentMessage handleDeposit(@DestinationVariable String userId, PaymentMessage message) {
        // Lógica de manejo de pago, por ejemplo, actualización del estado o registro en base de datos
        System.out.println("Payment received from: " + message.getSender() + " to " + message.getReceiver() +
                " Amount: " + message.getAmount() + " State: " + message.getState() + " Date: " + message.getDate());
        this.paymentService.insertPayment(message);
        return new PaymentMessage(message.getSender(), message.getReceiver(), message.getAmount(), message.getState(), message.getDate());
    }

    @SendTo("/topic/deposit/notification/{userId}")
    public static Payment handleDeposit(@DestinationVariable String userId, Payment deposit) {
        // Lógica de manejo de pago, por ejemplo, actualización del estado o registro en base de datos
        //userId = payment.getSender().getId().toString();
        System.out.println("Deposit notification");
        return deposit;
    }

    @MessageMapping("/deposit/user/{userId}")
    @SendTo("/topic/deposit/user/{userId}")
    public Iterable<Payment> getDeposits(@DestinationVariable String userId, User user) {
        // Lógica de manejo de pago, por ejemplo, actualización del estado o registro en base de datos
        System.out.println("List deposits");
        Iterable<Payment> deposits = depositService.getDeposits(user);
        System.out.println(deposits);
        return deposits;
    }
}

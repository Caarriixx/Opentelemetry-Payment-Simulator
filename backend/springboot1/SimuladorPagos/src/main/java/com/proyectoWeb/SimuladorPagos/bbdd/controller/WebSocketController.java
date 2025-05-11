package com.proyectoWeb.SimuladorPagos.bbdd.controller;

import com.proyectoWeb.SimuladorPagos.bbdd.model.LoginMessage;
import com.proyectoWeb.SimuladorPagos.bbdd.model.PaymentMessage;
import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import com.proyectoWeb.SimuladorPagos.bbdd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private UserService userService;

    // Metodo para manejo general de mensajes de usuario
//    @MessageMapping("/log/{userId}")
//    @SendTo("/topic/login/{userId}")  // Enviar a todos los suscriptores del canal específico
//    public LoginMessage user(@DestinationVariable String userId, LoginMessage message) {
//        System.out.println("Contraseña1: " + message.getPassword() + "\nUsuario: " + message.getUserId());
//        //logica manejo de login
//        User user = userService.findByName(message.getUserId()).orElse(null);
//
//        //System.out.println("El metodo del controlador ha sido invocado para el usuario: " + userId);
//        if (user != null) {
//            System.out.println(user);
//            System.out.println("Contraseña2: " + message.getPassword() + "\nUsuario: " + message.getUserId());
//
//            if(user.getName().equals(message.getUserId()) && user.getPassword().equals(message.getPassword())){
//                return message;
//            }
//        } else {
//            System.out.println("El objeto Login es nulo");
//        }
//        return new LoginMessage(null, null);
//    }
    //Manejo de mensajes para el pago, ns si se puede tener dos @SendTo("/topic/{userId}") con mismo path
//    @MessageMapping("/payment/{userId}")
//    @SendTo("/topic/payment/{userId}")
//    public PaymentMessage handlePayment(@DestinationVariable String userId, PaymentMessage message) {
//        System.out.println("Payment received from: " + message.getSender() +
//                " to " + message.getRecipient() +
//                " Amount: " + message.getAmount() +
//                " State: " + message.getState() +
//                " Date: " + message.getDate());
//
//        // Lógica de manejo de pago, por ejemplo, buscar usuario, actualizar estado del pago, etc.
//        User sender = userService.findByName(message.getSender().getName()).orElse(null);
//        User recipient = userService.findByName(message.getRecipient().getName()).orElse(null);
//
//        if (sender != null && recipient != null) {
//            // Aquí puedes agregar lógica específica, como actualizar el saldo, registrar el pago, etc.
//            System.out.println("Sender and recipient found.");
//            return message; // Retornas el mensaje tal cual, o puedes crear una respuesta modificada.
//        } else {
//            System.out.println("Sender or recipient not found.");
//        }
//        return new PaymentMessage(null, null, 0, 0, null);
//    }


}


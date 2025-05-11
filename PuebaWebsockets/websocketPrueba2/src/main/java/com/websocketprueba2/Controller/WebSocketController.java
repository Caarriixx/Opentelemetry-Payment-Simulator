package com.websocketprueba2.Controller;

import com.websocketprueba2.dto.LoginMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

    @Controller
    public class WebSocketController {

        // Método para manejo general de mensajes de usuario
        @MessageMapping("/log/{userId}")
        @SendTo("/topic/{userId}")  // Enviar a todos los suscriptores del canal específico
        public LoginMessage user(@DestinationVariable String userId, LoginMessage message) {
            System.out.println("El método del controlador ha sido invocado para el usuario: " + userId);
            if (message != null) {
                System.out.println("Contraseña: " + message.getPassword() + "\nUsuario: " + message.getUserId());
            } else {
                System.out.println("El objeto Login es nulo");
            }
            return new LoginMessage(message.getUserId(), message.getPassword());
        }
        /*
        // Método para notificaciones individuales
        @MessageMapping("/log/notify/{userId}")
        @SendToUser("/queue/notifications")
        public LoginMessage handleLogin(@DestinationVariable String userId, LoginMessage message) {
            System.out.println("El método handleLogin ha sido invocado para el usuario: " + userId);
            return new LoginMessage(message.getUserId(), "Inicio de sesión correcto");
        }
        */
    }

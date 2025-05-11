package com.proyectoWeb.SimuladorPagos.bbdd.controller;

import com.proyectoWeb.SimuladorPagos.bbdd.model.LoginMessage;
import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import com.proyectoWeb.SimuladorPagos.bbdd.service.UserService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("login-tracer");

    private final Meter meter;
    private LongCounter loginCounter;

    @Autowired
    UserService userService;

    public UserController(BCryptPasswordEncoder passwordEncoder, Meter meter) {
        this.passwordEncoder = passwordEncoder;
        this.meter = meter;
    }

    @PostConstruct
    public void initMetrics() {
        loginCounter = meter.counterBuilder("login_attempts")
                .setDescription("Login attempts count")
                .setUnit("1")
                .build();
    }

    @MessageMapping("/log/{sesionID}")
    @SendTo("/topic/login/{sesionID}")
    public LoginMessage user(@DestinationVariable String sesionID, LoginMessage message) {
        Span span = tracer.spanBuilder("Login Attempt")
                .setAttribute("login.email", message.getUserName())
                .setAttribute("login.sessionId", sesionID)
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            // Añadir traceId a los logs
            String traceId = span.getSpanContext().getTraceId();
            MDC.put("traceId", traceId);

            span.addEvent("Login request received");

            User user = userService.findByEmail(message.getUserName()).orElse(new User(-1));
            if (user.getId() == -1) {
                span.setAttribute("login.result", "error");
                span.setAttribute("login.error_type", "user_not_found");
                span.setStatus(StatusCode.ERROR, "User not found");

                loginCounter.add(1, Attributes.of(AttributeKey.stringKey("result"), "fail"));
                LOGGER.error("Login failed: user not found");
                return new LoginMessage(-1, message.getUserName(), message.getPassword());
            }

            if (user.getEmail().equals(message.getUserName()) &&
                    passwordEncoder.matches(message.getPassword(), user.getPassword())) {

                span.setAttribute("login.result", "success");
                span.setAttribute("login.user_id", user.getId());
                loginCounter.add(1, Attributes.of(AttributeKey.stringKey("result"), "success"));

                message.setUserId(user.getId());
                LOGGER.info("Login successful for user {}", user.getEmail());
                return message;
            } else {
                span.setAttribute("login.result", "error");
                span.setAttribute("login.error_type", "wrong_password");
                span.setStatus(StatusCode.ERROR, "Wrong password");

                loginCounter.add(1, Attributes.of(AttributeKey.stringKey("result"), "fail"));
                LOGGER.error("Login failed: incorrect password for {}", user.getEmail());
                return new LoginMessage(-1, message.getUserName(), message.getPassword());
            }

        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "Unexpected error during login");
            span.setAttribute("login.result", "error");
            span.setAttribute("login.error_type", "exception");

            loginCounter.add(1, Attributes.of(AttributeKey.stringKey("result"), "fail"));
            LOGGER.error("Unexpected error during login", e);
            return new LoginMessage(-1, message.getUserName(), message.getPassword());
        } finally {
            span.end();
            MDC.remove("traceId");
        }
    }

    @MessageMapping("/user/{userId}/{sesionId}")
    @SendTo("/topic/user/{userId}/{sesionId}")
    public User getUserById(@DestinationVariable String sesionId, Integer userId) {
        System.out.println("Usuario con id: " + userId + " solicitado");
        User user = userService.findByID(userId).orElse(null);
        System.out.println("Se devuelve el usuario: " + user);
        return user;
    }

    @MessageMapping("/user/list/{sesionId}")
    @SendTo("/topic/users/{sesionId}")
    public Iterable<User> getUsers(@DestinationVariable String sesionId) {
        System.out.println("Se solicita lista de usuarios");
        return userService.listUsers();
    }
}

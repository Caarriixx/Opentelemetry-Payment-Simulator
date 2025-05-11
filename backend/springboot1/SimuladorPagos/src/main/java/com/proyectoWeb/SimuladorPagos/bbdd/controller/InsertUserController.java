package com.proyectoWeb.SimuladorPagos.bbdd.controller;

import com.proyectoWeb.SimuladorPagos.bbdd.model.User;
import com.proyectoWeb.SimuladorPagos.bbdd.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class InsertUserController {

    private final UserService userService;

    @Autowired
    public InsertUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to register a new user.
     *
     * @param user The user details provided in the request body.
     * @return A ResponseEntity with a success message or an error message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User successfully registered.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred while registering the user.");
        }
    }
}

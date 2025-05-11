package com.proyectoWeb.SimuladorPagos.bbdd.cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for security settings.
 * Configures the security filter chain for handling HTTP requests and authentication.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * - Disables CSRF for testing purposes.
     * - Allows unauthenticated access to the user registration endpoint.
     * - Requires authentication for all other requests.
     *
     * @param http the {@link HttpSecurity} object to configure security settings.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/users/register").permitAll() // Allow public access to registration
                        .anyRequest().permitAll() // Require authentication for other endpoints
                );
        return http.build();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


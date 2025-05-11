package com.proyectoWeb.SimuladorPagos.opentelemetry;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Scheduler that automatically calls /otel/trace every minute to generate traces.
 */
@Component
public class TraceScheduler {

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 60000) // Cada 60000ms = 60s
    public void generateTrace() {
        try {
            String response = restTemplate.getForObject("http://localhost:8080/otel/trace", String.class);
            System.out.println("Trace generated: " + response);
        } catch (Exception e) {
            System.out.println("Error generating trace: " + e.getMessage());
        }
    }
}

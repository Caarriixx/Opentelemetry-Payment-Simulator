package com.proyectoWeb.SimuladorPagos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Main class for the Simulador Pagos application.
 * <p>
 * This class serves as the entry point for the Spring Boot application. It is
 * annotated with {@link SpringBootApplication}, which enables auto-configuration,
 * component scanning, and configuration support.
 */
@SpringBootApplication
@EnableScheduling
public class SimuladorPagosApplication {

	/**
	 * Main method to launch the Simulador Pagos application.
	 * <p>
	 * This method initializes the Spring Boot application context and starts the application.
	 *
	 * @param args Command-line arguments passed during application startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SimuladorPagosApplication.class, args);
	}
}


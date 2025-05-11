package com.backend.centroautorizador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the CentroAutorizador application.
 * <p>
 * This class initializes and runs the Spring Boot application. It is annotated
 * with {@link SpringBootApplication}, which encompasses configuration, component
 * scanning, and auto-configuration.
 * </p>
 */
@SpringBootApplication
public class CentroAutorizadorApplication {

	/**
	 * Main method to launch the Spring Boot application.
	 *
	 * @param args Command-line arguments passed during application startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(CentroAutorizadorApplication.class, args);
	}
}



package com.proyectoWeb.SimuladorPagos.elastic.contoller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.proyectoWeb.SimuladorPagos.elastic.service.ErrorLogService;
import com.proyectoWeb.SimuladorPagos.elastic.entity.ErrorLog;



@RestController
@RequestMapping("/api/errors")
public class ErrorLogController {

    @Autowired
    private ErrorLogService errorLogService;

    @Operation(summary = "Log an error", description = "Logs an error with type, message, and details.")
    @PostMapping("/log")
    public String logError(
            @Parameter(description = "Type of the error", example = "ValidationError") @RequestParam String type,
            @Parameter(description = "Error message", example = "Invalid input provided") @RequestParam String message,
            @Parameter(description = "Detailed error description", example = "Field 'username' is required") @RequestParam String details) {
        errorLogService.logError(type, message, details);
        return "Error logged successfully";
    }

    @Operation(summary = "Get all errors", description = "Fetches all logged errors.")
    @GetMapping("/all")
    public Iterable<ErrorLog> getAllErrors() {
        return errorLogService.getAllErrors();
    }
}


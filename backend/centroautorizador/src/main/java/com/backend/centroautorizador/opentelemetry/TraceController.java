package com.backend.centroautorizador.opentelemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otel")
public class TraceController {

    private final Tracer tracer;

    public TraceController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/trace")
    public String createTrace() {
        Span span = tracer.spanBuilder("manual-test-span")
                .startSpan();
        try {
            span.addEvent("Start processing request"); // Añade un evento a la traza
            Thread.sleep(500); // Simulación de carga
            span.addEvent("Finished processing request"); // Añade un evento a la traza
            return "Trace created!";
        } catch (InterruptedException e) {
            span.recordException(e); // Registra la excepción en la traza
            Thread.currentThread().interrupt();
        } finally {
            span.end();
        }
        return "Error creating trace";
    }


}

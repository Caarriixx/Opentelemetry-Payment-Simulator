package com.backend.centroautorizador.opentelemetry;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

@Configuration
public class OpenTelemetryConfig {
/*
    @Bean
    public OpenTelemetry openTelemetry() {
        // 🔄 Crea el span exporter apuntando al collector dentro del contenedor
        OtlpHttpSpanExporter spanExporter = OtlpHttpSpanExporter.builder()
                .setEndpoint("http://apm-server:8200")
                .setTimeout(java.time.Duration.ofSeconds(10))
                .build();

        // 🏷️ Define el nombre del servicio
        Resource serviceResource = Resource.getDefault()
                .merge(Resource.create(Attributes.of(
                        ResourceAttributes.SERVICE_NAME, "simulador-pagos"
                )));

        // ⚙️ Crea el provider con el recurso y el exporter
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
                .setResource(serviceResource)
                .build();

        // 🔧 Devuelve una instancia del SDK configurada
        return OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .build();
    }
*/
    @Bean
    public Meter meter() {
        return GlobalOpenTelemetry.get().getMeter("login-metrics");
    }
    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("simulador-pagos");
    }

    @Bean
    @Primary
    public ParameterNameDiscoverer parameterNameDiscoverer() {
        return new DefaultParameterNameDiscoverer();
    }
}

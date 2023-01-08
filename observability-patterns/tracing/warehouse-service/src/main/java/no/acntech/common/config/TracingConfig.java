package no.acntech.common.config;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot Actuator does not yet provide autoconfiguration for a pure OTLP setup.
 * As of Spring Boot 3.0.1 they only support Zipkin and Wavefront out of the box.
 * Due to this shortcoming we need to create an OTLP span exporter which is then used by
 * the OTLP Micrometer bridge.
 */
@ConditionalOnProperty(name = "management.tracing.enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class TracingConfig {

    @ConditionalOnProperty(name = "management.otlp.tracing.enabled", havingValue = "true", matchIfMissing = true)
    @EnableConfigurationProperties(OtlpTracingProperties.class)
    @Configuration(proxyBeanMethods = false)
    public static class OltpTracingConfig {

        @Bean
        public OtlpGrpcSpanExporter otlpGrpcSpanExporter(final OtlpTracingProperties properties) {
            return OtlpGrpcSpanExporter.builder()
                    .setEndpoint(properties.getExport().getGrpc().getUrl())
                    .setTimeout(properties.getExport().getGrpc().getTimeout())
                    .build();
        }
    }
}

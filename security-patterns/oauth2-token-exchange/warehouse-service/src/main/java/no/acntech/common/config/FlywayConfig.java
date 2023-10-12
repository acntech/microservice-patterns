package no.acntech.common.config;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot does not configure Flyway to search for Java callbacks in the defined locations.
 * This customizer overrides this behavior so that callbacks can be placed in packages other than 'db.callback'.
 * This allows for controlling callback activation by Spring profiles.
 */
@Configuration(proxyBeanMethods = false)
public class FlywayConfig implements FlywayConfigurationCustomizer {

    private final FlywayProperties properties;

    public FlywayConfig(final FlywayProperties properties) {
        this.properties = properties;
    }

    @Override
    public void customize(final FluentConfiguration configuration) {
        final var locations = properties.getLocations().stream()
                .map(location -> location.replace("classpath:", ""))
                .toArray(String[]::new);
        configuration.callbacks(locations);
    }
}

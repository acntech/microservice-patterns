package no.acntech.common.config;

import no.acntech.common.converter.UUIDAttributeConverter;
import no.acntech.common.converter.ZonedDateTimeAttributeConverter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
        "no.acntech.product.repository",
        "no.acntech.reservation.repository"
})
@EntityScan(
        basePackageClasses = {
                Jsr310JpaConverters.class,
                ZonedDateTimeAttributeConverter.class,
                UUIDAttributeConverter.class},
        basePackages = {
                "no.acntech.product.model",
                "no.acntech.inventory.model",
                "no.acntech.reservation.model"
        })
@Configuration(proxyBeanMethods = false)
public class DatabaseConfig {
}

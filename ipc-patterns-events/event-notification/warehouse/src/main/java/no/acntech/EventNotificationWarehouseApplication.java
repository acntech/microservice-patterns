package no.acntech;

import no.acntech.converter.UUIDAttributeConverter;
import no.acntech.converter.ZonedDateTimeAttributeConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = {
        "no.acntech.product.repository"
})
@EntityScan(
        basePackageClasses = {
                Jsr310JpaConverters.class,
                ZonedDateTimeAttributeConverter.class,
                UUIDAttributeConverter.class},
        basePackages = {
                "no.acntech.product.model"
        })
@EnableTransactionManagement
@SpringBootApplication
public class EventNotificationWarehouseApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EventNotificationWarehouseApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EventNotificationWarehouseApplication.class);
    }
}

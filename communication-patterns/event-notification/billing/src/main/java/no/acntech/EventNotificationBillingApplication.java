package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAsync
@EnableTransactionManagement
@SpringBootApplication
public class EventNotificationBillingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EventNotificationBillingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EventNotificationBillingApplication.class);
    }
}
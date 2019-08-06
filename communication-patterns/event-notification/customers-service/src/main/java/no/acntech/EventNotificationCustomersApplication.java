package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EventNotificationCustomersApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EventNotificationCustomersApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EventNotificationCustomersApplication.class);
    }
}

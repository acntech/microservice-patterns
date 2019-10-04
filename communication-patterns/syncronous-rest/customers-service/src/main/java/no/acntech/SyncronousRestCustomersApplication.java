package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SyncronousRestCustomersApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SyncronousRestCustomersApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SyncronousRestCustomersApplication.class);
    }
}

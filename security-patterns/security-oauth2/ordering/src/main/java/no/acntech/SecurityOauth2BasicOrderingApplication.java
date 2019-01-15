package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class SecurityOauth2BasicOrderingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauth2BasicOrderingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SecurityOauth2BasicOrderingApplication.class);
    }
}

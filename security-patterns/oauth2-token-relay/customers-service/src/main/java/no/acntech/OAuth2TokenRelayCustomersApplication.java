package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class OAuth2TokenRelayCustomersApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2TokenRelayCustomersApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OAuth2TokenRelayCustomersApplication.class);
    }
}

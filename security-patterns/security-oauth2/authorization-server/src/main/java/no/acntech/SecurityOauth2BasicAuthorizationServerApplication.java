package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SecurityOauth2BasicAuthorizationServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauth2BasicAuthorizationServerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SecurityOauth2BasicAuthorizationServerApplication.class);
    }
}

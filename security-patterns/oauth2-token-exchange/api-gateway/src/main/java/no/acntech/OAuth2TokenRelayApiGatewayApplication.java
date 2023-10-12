package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {ReactiveUserDetailsServiceAutoConfiguration.class})
public class OAuth2TokenRelayApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2TokenRelayApiGatewayApplication.class, args);
    }
}

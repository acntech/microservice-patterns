package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class OAuth2TokenRelayOrderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2TokenRelayOrderingApplication.class, args);
    }
}

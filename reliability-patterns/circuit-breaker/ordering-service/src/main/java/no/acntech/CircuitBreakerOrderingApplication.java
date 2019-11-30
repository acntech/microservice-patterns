package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class CircuitBreakerOrderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CircuitBreakerOrderingApplication.class, args);
    }
}

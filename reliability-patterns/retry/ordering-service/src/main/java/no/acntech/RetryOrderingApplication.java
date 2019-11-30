package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableRetry
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class RetryOrderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetryOrderingApplication.class, args);
    }
}

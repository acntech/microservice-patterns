package no.acntech.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@EnableRetry
public class ReliabilityRetryOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReliabilityRetryOrderApplication.class, args);
    }
}

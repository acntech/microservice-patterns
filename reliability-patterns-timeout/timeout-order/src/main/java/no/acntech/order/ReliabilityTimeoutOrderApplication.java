package no.acntech.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class ReliabilityTimeoutOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReliabilityTimeoutOrderApplication.class, args);
    }
}

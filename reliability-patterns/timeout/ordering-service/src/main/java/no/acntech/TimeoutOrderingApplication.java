package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class TimeoutOrderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeoutOrderingApplication.class, args);
    }
}

package no.acntech.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class RectiveOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RectiveOrderApplication.class, args);
    }
}

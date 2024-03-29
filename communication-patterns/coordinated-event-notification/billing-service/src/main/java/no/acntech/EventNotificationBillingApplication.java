package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class EventNotificationBillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventNotificationBillingApplication.class, args);
    }
}

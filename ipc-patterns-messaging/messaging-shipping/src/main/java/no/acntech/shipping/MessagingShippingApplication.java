package no.acntech.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class MessagingShippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagingShippingApplication.class, args);
    }

}

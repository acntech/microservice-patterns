package no.acntech.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJms
public class MessagingOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagingOrderApplication.class, args);
    }

}

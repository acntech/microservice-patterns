package no.acntech.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableMongoAuditing
public class RectiveOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RectiveOrderApplication.class, args);
    }
}

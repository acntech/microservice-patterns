package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class TracingWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracingWarehouseApplication.class, args);
    }

}

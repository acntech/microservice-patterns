package no.acntech.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import no.acntech.reservation.model.Inventory;
import no.acntech.reservation.repository.InventoryRepository;

@SpringBootApplication
public class MessagingWarehouseApplication {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public MessagingWarehouseApplication(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MessagingWarehouseApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Inventory p1 = Inventory.builder()
                    .productId("42")
                    .quantity(10)
                    .build();

            Inventory p2 = Inventory.builder()
                    .productId("43")
                    .quantity(5)
                    .build();

            inventoryRepository.save(p1);
            inventoryRepository.save(p2);
        };
    }
}

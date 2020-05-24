package no.acntech.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import no.acntech.warehouse.entity.Inventory;
import no.acntech.warehouse.repository.InventoryRepository;

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
            Inventory p1 = new Inventory();
            p1.setProductId("42");
            p1.setQuantity(10);

            Inventory p2 = new Inventory();
            p2.setProductId("43");
            p2.setQuantity(5);

            inventoryRepository.save(p1);
            inventoryRepository.save(p2);
        };
    }
}

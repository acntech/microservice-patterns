package no.acntech.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import no.acntech.warehouse.repository.InventoryRepository;

@SpringBootApplication
@EnableMongoAuditing
public class ReactiveWarehouseApplication {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public ReactiveWarehouseApplication(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveWarehouseApplication.class, args);
    }

}

package no.acntech.testdata;

import javax.annotation.PostConstruct;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import no.acntech.product.model.Inventory;
import no.acntech.product.repository.InventoryRepository;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;

@Component
public class TestData {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public TestData(final ProductRepository productRepository,
                    final InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @PostConstruct
    public void insertTestData() {
        String[] productNames = {
                "Product 1",
                "Product 2",
                "Product 3",
                "Product 4",
                "Product 5"
        };

        Arrays.stream(productNames)
                .map(name -> Product.builder()
                        .name(name)
                        .build())
                .map(productRepository::save)
                .map(product -> Inventory.builder()
                        .product(product)
                        .quantity(randomQuantity())
                        .build())
                .forEach(inventoryRepository::save);
    }

    private long randomQuantity() {
        int min = 0;
        int max = 1000;
        return (long) ((Math.random() * ((max - min) + 1)) + min);
    }
}

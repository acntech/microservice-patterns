package no.acntech.testdata;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import no.acntech.inventory.model.Inventory;
import no.acntech.inventory.repository.InventoryRepository;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;

@SuppressWarnings("Duplicates")
@Component
public class TestData {

    private static final List<String> PRODUCT_NAMES = Arrays.asList("Product 1", "Product 2", "Product 3", "Product 4", "Product 5", "Product 6", "Product 7", "Product 8", "Product 9");

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public TestData(final ProductRepository productRepository,
                    final InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @PostConstruct
    private void insertTestData() {

        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {

            PRODUCT_NAMES.stream()
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
    }

    private long randomQuantity() {
        int min = 0;
        int max = 1000;
        return (long) ((Math.random() * ((max - min) + 1)) + min);
    }
}

package no.acntech.testdata;

import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import no.acntech.product.model.Currency;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;

@SuppressWarnings("Duplicates")
@Profile("test-data")
@Component
public class TestData {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestData.class);
    private static final List<String> PRODUCT_NAMES = Arrays.asList("Product 1", "Product 2", "Product 3", "Product 4", "Product 5", "Product 6", "Product 7", "Product 8", "Product 9");

    private final ProductRepository productRepository;

    public TestData(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    private void insertTestData() {
        LOGGER.info("Inserting test data");

        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {

            PRODUCT_NAMES.stream()
                    .map(name -> Product.builder()
                            .name(name)
                            .description("Description for " + name)
                            .stock(randomStock())
                            .price(randomPrice())
                            .currency(Currency.USD)
                            .build())
                    .forEach(productRepository::save);
        }
    }

    private long randomStock() {
        int min = 0;
        int max = 10000;
        return (long) ((Math.random() * ((max - min) + 1)) + min);
    }

    private BigDecimal randomPrice() {
        int min = 10;
        int max = 100000;
        Random random = new Random();
        double randomDouble = min + random.nextDouble() * (max - min);
        return BigDecimal.valueOf(randomDouble);
    }
}

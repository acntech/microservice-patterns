package no.acntech.product.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.CreateProduct;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(@NotNull final UUID prductId) {
        return productRepository.findByProductId(prductId)
                .orElseThrow(() -> new ProductNotFoundException(prductId));
    }

    public Product createProduct(@NotNull final CreateProduct createProduct) {
        Product product = Product.builder()
                .name(createProduct.getName())
                .description(createProduct.getDescription())
                .build();
        return productRepository.save(product);
    }
}

package no.acntech.product.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.Product;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.repository.ProductRepository;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findProducts(@NotNull final ProductQuery productQuery) {
        String name = productQuery.getName();
        if (name != null) {
            return productRepository.findAllByName(name, SORT_BY_ID);
        } else {
            return productRepository.findAll(SORT_BY_ID);
        }
    }

    public Product getProduct(@NotNull final UUID productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Product createProduct(@NotNull final CreateProductDto createProduct) {
        Product product = Product.builder()
                .name(createProduct.getName())
                .description(createProduct.getDescription())
                .build();
        return productRepository.save(product);
    }
}

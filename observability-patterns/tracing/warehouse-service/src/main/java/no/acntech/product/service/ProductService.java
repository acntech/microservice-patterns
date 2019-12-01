package no.acntech.product.service;

import brave.ScopedSpan;
import brave.Tracer;
import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.Product;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");

    private final Tracer tracer;
    private final ProductRepository productRepository;

    public ProductService(final Tracer tracer,
                          final ProductRepository productRepository) {
        this.tracer = tracer;
        this.productRepository = productRepository;
    }

    public List<Product> findProducts(@NotNull final ProductQuery productQuery) {
        ScopedSpan span = tracer.startScopedSpan("ProductService#findProducts");
        try {
            String name = productQuery.getName();
            if (name != null) {
                return productRepository.findAllByName(name);
            } else {
                return productRepository.findAll(SORT_BY_ID);
            }
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public Product getProduct(@NotNull final UUID productId) {
        ScopedSpan span = tracer.startScopedSpan("ProductService#getProduct");
        try {
            return productRepository.findByProductId(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public Product createProduct(@NotNull final CreateProductDto createProduct) {
        ScopedSpan span = tracer.startScopedSpan("ProductService#createProduct");
        try {
            Product product = Product.builder()
                    .name(createProduct.getName())
                    .description(createProduct.getDescription())
                    .build();
            return productRepository.save(product);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }
}

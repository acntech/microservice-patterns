package no.acntech.product.service;

import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.CreateProduct;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    public ProductService(final ConversionService conversionService,
                          final ProductRepository productRepository) {
        this.conversionService = conversionService;
        this.productRepository = productRepository;
    }

    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(final UUID prductId) {
        return productRepository.findByProductId(prductId)
                .orElseThrow(() -> new ProductNotFoundException(prductId));
    }

    public Product createProduct(final CreateProduct createProduct) {
        Product product = conversionService.convert(createProduct, Product.class);
        return productRepository.save(product);
    }
}

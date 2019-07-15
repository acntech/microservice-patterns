package no.acntech.product.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.Product;
import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.repository.ProductRepository;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    public ProductService(final ConversionService conversionService,
                          final ProductRepository productRepository) {
        this.conversionService = conversionService;
        this.productRepository = productRepository;
    }

    public List<ProductDto> findProducts(@NotNull final ProductQuery productQuery) {
        String name = productQuery.getName();
        if (name != null) {
            return productRepository.findAllByName(name)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return productRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public ProductDto getProduct(@NotNull final UUID productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return convert(product);
    }

    public ProductDto createProduct(@NotNull final CreateProductDto createProduct) {
        Product product = Product.builder()
                .name(createProduct.getName())
                .description(createProduct.getDescription())
                .build();

        Product savedProduct = productRepository.save(product);

        LOGGER.info("Created product with product-id {}", savedProduct.getProductId());

        return convert(savedProduct);
    }

    private ProductDto convert(final Product product) {
        return conversionService.convert(product, ProductDto.class);
    }
}

package no.acntech.product.service;

import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductEntity;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.repository.ProductRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    public ProductService(final ConversionService conversionService,
                          final ProductRepository productRepository) {
        this.conversionService = conversionService;
        this.productRepository = productRepository;
    }

    public List<ProductDto> findProducts(@NotNull @Valid final ProductQuery productQuery) {
        if (productQuery.getName() != null) {
            return productRepository.findAllByName(productQuery.getName())
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
        return productRepository.findByProductId(productId)
                .map(this::convert)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    public ProductDto createProduct(@NotNull @Valid final CreateProductDto createProductDto) {
        final var productEntity = conversionService.convert(createProductDto, ProductEntity.class);
        Assert.notNull(productEntity, "Failed to convert CreateProductDto to ProductEntity");
        final var savedProductEntity = productRepository.save(productEntity);
        return convert(savedProductEntity);
    }

    private ProductDto convert(final ProductEntity productEntity) {
        final var productDto = conversionService.convert(productEntity, ProductDto.class);
        Assert.notNull(productDto, "Failed to convert ProductEntity to ProductDto");
        return productDto;
    }
}

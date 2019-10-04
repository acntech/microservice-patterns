package no.acntech.product.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.Product;
import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.service.ProductService;

@RequestMapping(path = "products")
@RestController
public class ProductsResource {

    private final ConversionService conversionService;
    private final ProductService productService;

    public ProductsResource(final ConversionService conversionService,
                            final ProductService productService) {
        this.conversionService = conversionService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> find(final ProductQuery productQuery) {
        final List<Product> products = productService.findProducts(productQuery);
        final List<ProductDto> productDtos = products.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ProductDto> get(@Valid @NotNull @PathVariable("id") final UUID productId) {
        final Product product = productService.getProduct(productId);
        final ProductDto productDto = convert(product);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateProductDto createProduct) {
        final Product product = productService.createProduct(createProduct);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getProductId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    private ProductDto convert(final Product product) {
        return conversionService.convert(product, ProductDto.class);
    }
}

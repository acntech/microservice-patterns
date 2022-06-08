package no.acntech.product.resource;

import brave.ScopedSpan;
import brave.Tracer;
import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.Product;
import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.service.ProductService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping(path = "products")
@RestController
public class ProductsResource {

    private final Tracer tracer;
    private final ConversionService conversionService;
    private final ProductService productService;

    public ProductsResource(final Tracer tracer,
                            final ConversionService conversionService,
                            final ProductService productService) {
        this.tracer = tracer;
        this.conversionService = conversionService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> find(final ProductQuery productQuery) {
        ScopedSpan span = tracer.startScopedSpan("ProductsResource#find");
        try {
            final List<Product> products = productService.findProducts(productQuery);
            final List<ProductDto> productDtos = products.stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDtos);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductDto> get(@Valid @NotNull @PathVariable("productId") final UUID productId) {
        ScopedSpan span = tracer.startScopedSpan("ProductsResource#get");
        try {
            final Product product = productService.getProduct(productId);
            final ProductDto productDto = convert(product);
            return ResponseEntity.ok(productDto);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody final CreateProductDto createProduct) {
        ScopedSpan span = tracer.startScopedSpan("ProductsResource#post");
        try {
            final Product product = productService.createProduct(createProduct);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{productId}")
                    .buildAndExpand(product.getProductId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    private ProductDto convert(final Product product) {
        return conversionService.convert(product, ProductDto.class);
    }
}

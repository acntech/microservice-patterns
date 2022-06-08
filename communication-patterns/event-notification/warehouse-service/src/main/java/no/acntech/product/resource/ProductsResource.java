package no.acntech.product.resource;

import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/products")
@RestController
public class ProductsResource {

    private final ProductService productService;

    public ProductsResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> find(final ProductQuery productQuery) {
        final List<ProductDto> productDtos = productService.findProducts(productQuery);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductDto> get(@PathVariable("productId") final UUID productId) {
        final var productDto = productService.getProduct(productId);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody final CreateProductDto createProduct) {
        final var productDto = productService.createProduct(createProduct);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .pathSegment(productDto.getProductId().toString())
                .build()
                .toUri();
        return ResponseEntity
                .created(location)
                .body(productDto);
    }
}

package no.acntech.product.resource;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductQuery;
import no.acntech.product.service.ProductService;

@RequestMapping(path = "products")
@RestController
public class ProductsResource {

    private final ProductService productService;

    public ProductsResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> find(@NotNull ProductQuery productQuery,
                                                 @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        List<ProductDto> products = productService.findProducts(productQuery, authorizedClient);
        return ResponseEntity.ok(products);
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductDto> get(@PathVariable("productId") final UUID productId,
                                          @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        final ProductDto product = productService.getProduct(productId, authorizedClient);
        return ResponseEntity.ok(product);
    }
}

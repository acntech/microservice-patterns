package no.acntech.product.resource;

import no.acntech.product.consumer.ProductConsumer;
import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping(path = "/api/products")
@RestController
public class ProductsResource {

    private final ProductConsumer productConsumer;

    public ProductsResource(final ProductConsumer productConsumer) {
        this.productConsumer = productConsumer;
    }

    @GetMapping(path = "{productId}")
    public Mono<ProductDto> get(@PathVariable("productId") final UUID productId) {
        return productConsumer.getProduct(productId);
    }

    @GetMapping
    public Flux<ProductDto> find(final ProductQuery productQuery) {
        return productConsumer.findProducts(productQuery);
    }
}

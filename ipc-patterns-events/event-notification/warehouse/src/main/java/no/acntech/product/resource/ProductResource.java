package no.acntech.product.resource;

import no.acntech.product.model.Product;
import no.acntech.product.service.ProductService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "products")
@RestController
public class ProductResource {

    private final ProductService productService;

    public ProductResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> get() {
        return productService.findProducts();
    }
}

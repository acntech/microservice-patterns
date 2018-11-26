package no.acntech.product.converter;

import no.acntech.product.model.CreateProduct;
import no.acntech.product.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class CreateProductConverter implements Converter<CreateProduct, Product> {

    @Override
    public Product convert(@Valid @NotNull final CreateProduct createProduct) {
        return Product.builder()
                .name(createProduct.getName())
                .description(createProduct.getDescription())
                .build();
    }
}

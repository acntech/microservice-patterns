package no.acntech.product.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.product.model.Product;
import no.acntech.product.model.ProductDto;

@Component
public class ProductDtoConverter implements Converter<Product, ProductDto> {

    @Override
    public ProductDto convert(@NonNull final Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .created(product.getCreated())
                .modified(product.getModified())
                .build();
    }
}

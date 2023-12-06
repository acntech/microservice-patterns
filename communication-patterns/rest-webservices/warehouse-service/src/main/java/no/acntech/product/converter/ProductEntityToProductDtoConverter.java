package no.acntech.product.converter;

import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityToProductDtoConverter implements Converter<ProductEntity, ProductDto> {

    @NonNull
    @Override
    public ProductDto convert(@NonNull final ProductEntity source) {
        return ProductDto.builder()
                .productId(source.getProductId())
                .code(source.getCode())
                .name(source.getName())
                .description(source.getDescription())
                .stock(source.getStock())
                .packaging(source.getPackaging())
                .quantity(source.getQuantity())
                .measure(source.getMeasure())
                .price(source.getPrice())
                .currency(source.getCurrency())
                .created(source.getCreated())
                .modified(source.getModified())
                .build();
    }
}

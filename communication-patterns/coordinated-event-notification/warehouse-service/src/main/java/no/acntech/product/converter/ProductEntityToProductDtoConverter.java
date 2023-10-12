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
    public ProductDto convert(@NonNull final ProductEntity productEntity) {
        return ProductDto.builder()
                .productId(productEntity.getProductId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .stock(productEntity.getStock())
                .price(productEntity.getPrice())
                .currency(productEntity.getCurrency())
                .created(productEntity.getCreated())
                .modified(productEntity.getModified())
                .build();
    }
}

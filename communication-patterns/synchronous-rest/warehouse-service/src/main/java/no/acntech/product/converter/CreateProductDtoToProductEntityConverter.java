package no.acntech.product.converter;

import no.acntech.product.model.CreateProductDto;
import no.acntech.product.model.ProductEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateProductDtoToProductEntityConverter implements Converter<CreateProductDto, ProductEntity> {

    @NonNull
    @Override
    public ProductEntity convert(@NonNull final CreateProductDto source) {
        return ProductEntity.builder()
                .name(source.getName())
                .description(source.getDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .currency(source.getCurrency())
                .build();
    }
}

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
                .code(source.getCode().getValue())
                .name(source.getName().getValue())
                .description(source.getDescription().getValue())
                .stock(source.getStock().getValue())
                .packaging(source.getPackaging())
                .quantity(source.getQuantity().getValue())
                .measure(source.getMeasure())
                .price(source.getPrice().getValue())
                .currency(source.getCurrency())
                .build();
    }
}

package no.acntech.product.converter;

import com.google.protobuf.DoubleValue;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
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
        return ProductDto.newBuilder()
                .setProductId(StringValue.of(source.getProductId().toString()))
                .setCode(StringValue.of(source.getCode()))
                .setName(StringValue.of(source.getName()))
                .setDescription(StringValue.of(source.getDescription()))
                .setStock(UInt32Value.of(source.getStock()))
                .setPackaging(source.getPackaging())
                .setQuantity(UInt32Value.of(source.getQuantity()))
                .setMeasure(source.getMeasure())
                .setPrice(DoubleValue.of(source.getPrice()))
                .setCurrency(source.getCurrency())
                .setCreated(Timestamp.newBuilder()
                        .setSeconds(source.getCreated().toInstant().getEpochSecond())
                        .setNanos(source.getCreated().toInstant().getNano())
                        .build())
                .setModified(source.getModified() != null ? Timestamp.newBuilder()
                        .setSeconds(source.getModified().toInstant().getEpochSecond())
                        .setNanos(source.getModified().toInstant().getNano())
                        .build() : Timestamp.getDefaultInstance())
                .build();
    }
}

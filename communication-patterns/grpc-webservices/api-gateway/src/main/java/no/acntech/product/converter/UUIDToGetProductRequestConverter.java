package no.acntech.product.converter;

import com.google.protobuf.StringValue;
import no.acntech.product.model.GetProductHeader;
import no.acntech.product.model.GetProductRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToGetProductRequestConverter implements Converter<UUID, GetProductRequest> {

    @NonNull
    @Override
    public GetProductRequest convert(@NonNull final UUID source) {
        return GetProductRequest.newBuilder()
                .setHeader(GetProductHeader.newBuilder()
                        .setProductId(StringValue.of(source.toString()))
                        .build())
                .build();
    }
}

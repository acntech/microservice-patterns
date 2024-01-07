package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import no.acntech.order.model.GetOrderItemHeader;
import no.acntech.order.model.GetOrderItemRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToGetOrderItemRequestConverter implements Converter<UUID, GetOrderItemRequest> {

    @NonNull
    @Override
    public GetOrderItemRequest convert(@NonNull final UUID source) {
        return GetOrderItemRequest.newBuilder()
                .setHeader(GetOrderItemHeader.newBuilder()
                        .setItemId(StringValue.of(source.toString()))
                        .build())
                .build();
    }
}

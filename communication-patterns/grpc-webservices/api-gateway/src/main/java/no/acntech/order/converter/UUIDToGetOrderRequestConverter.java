package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import no.acntech.order.model.GetOrderHeader;
import no.acntech.order.model.GetOrderRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToGetOrderRequestConverter implements Converter<UUID, GetOrderRequest> {

    @NonNull
    @Override
    public GetOrderRequest convert(@NonNull final UUID source) {
        return GetOrderRequest.newBuilder()
                .setHeader(GetOrderHeader.newBuilder()
                        .setOrderId(StringValue.of(source.toString()))
                        .build())
                .build();
    }
}

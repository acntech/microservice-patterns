package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import no.acntech.order.model.GetOrderHeader;
import no.acntech.order.model.GetOrderRequest;
import no.acntech.order.model.UpdateOrderHeader;
import no.acntech.order.model.UpdateOrderRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToUpdateOrderRequestConverter implements Converter<UUID, UpdateOrderRequest> {

    @NonNull
    @Override
    public UpdateOrderRequest convert(@NonNull final UUID source) {
        return UpdateOrderRequest.newBuilder()
                .setHeader(UpdateOrderHeader.newBuilder()
                        .setOrderId(StringValue.of(source.toString()))
                        .build())
                .build();
    }
}

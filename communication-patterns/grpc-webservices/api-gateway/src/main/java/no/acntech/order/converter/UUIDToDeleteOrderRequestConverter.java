package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import no.acntech.order.model.DeleteOrderHeader;
import no.acntech.order.model.DeleteOrderRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToDeleteOrderRequestConverter implements Converter<UUID, DeleteOrderRequest> {

    @NonNull
    @Override
    public DeleteOrderRequest convert(@NonNull final UUID source) {
        return DeleteOrderRequest.newBuilder()
                .setHeader(DeleteOrderHeader.newBuilder()
                        .setOrderId(StringValue.of(source.toString()))
                        .build())
                .build();
    }
}

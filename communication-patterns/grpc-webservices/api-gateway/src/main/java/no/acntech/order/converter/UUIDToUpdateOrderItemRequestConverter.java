package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import groovy.lang.Tuple2;
import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.order.model.UpdateOrderItemHeader;
import no.acntech.order.model.UpdateOrderItemRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToUpdateOrderItemRequestConverter implements Converter<Tuple2<UUID, UpdateOrderItemDto>, UpdateOrderItemRequest> {

    @NonNull
    @Override
    public UpdateOrderItemRequest convert(@NonNull final Tuple2<UUID, UpdateOrderItemDto> source) {
        return UpdateOrderItemRequest.newBuilder()
                .setHeader(UpdateOrderItemHeader.newBuilder()
                        .setItemId(StringValue.of(source.getV1().toString()))
                        .build())
                .setBody(source.getV2())
                .build();
    }
}

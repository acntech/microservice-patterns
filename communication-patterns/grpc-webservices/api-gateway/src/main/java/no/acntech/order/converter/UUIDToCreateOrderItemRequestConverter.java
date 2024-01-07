package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import groovy.lang.Tuple2;
import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.CreateOrderItemRequest;
import no.acntech.order.model.CreateOrderItemsHeader;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToCreateOrderItemRequestConverter implements Converter<Tuple2<UUID, CreateOrderItemDto>, CreateOrderItemRequest> {

    @NonNull
    @Override
    public CreateOrderItemRequest convert(@NonNull final Tuple2<UUID, CreateOrderItemDto> source) {
        return CreateOrderItemRequest.newBuilder()
                .setHeader(CreateOrderItemsHeader.newBuilder()
                        .setOrderId(StringValue.of(source.getV1().toString()))
                        .build())
                .setBody(source.getV2())
                .build();
    }
}

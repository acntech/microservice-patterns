package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
import no.acntech.order.model.OrderItemDto;
import no.acntech.order.model.OrderItemEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderItemEntityToOrderItemDtoConverter implements Converter<OrderItemEntity, OrderItemDto> {

    @NonNull
    @Override
    public OrderItemDto convert(@NonNull final OrderItemEntity source) {
        return OrderItemDto.newBuilder()
                .setItemId(StringValue.of(source.getItemId().toString()))
                .setOrderId(StringValue.of(source.getParent().getOrderId().toString()))
                .setProductId(source.getProductId() != null ? StringValue.of(source.getProductId().toString()) : StringValue.getDefaultInstance())
                .setReservationId(StringValue.of(source.getReservationId().toString()))
                .setQuantity(UInt32Value.of(source.getQuantity()))
                .setStatus(source.getStatus())
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

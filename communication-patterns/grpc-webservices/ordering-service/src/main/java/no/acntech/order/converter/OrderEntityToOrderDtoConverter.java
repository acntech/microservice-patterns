package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEntity;
import no.acntech.order.model.OrderItemDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEntityToOrderDtoConverter implements Converter<OrderEntity, OrderDto> {

    private final OrderItemEntityToOrderItemDtoConverter orderItemEntityToOrderItemDtoConverter;

    public OrderEntityToOrderDtoConverter(final OrderItemEntityToOrderItemDtoConverter orderItemEntityToOrderItemDtoConverter) {
        this.orderItemEntityToOrderItemDtoConverter = orderItemEntityToOrderItemDtoConverter;
    }

    @NonNull
    @Override
    public OrderDto convert(@NonNull final OrderEntity source) {
        return OrderDto.newBuilder()
                .setOrderId(StringValue.of(source.getOrderId().toString()))
                .setCustomerId(StringValue.of(source.getCustomerId().toString()))
                .setName(StringValue.of(source.getName()))
                .setDescription(StringValue.of(source.getDescription()))
                .setStatus(source.getStatus())
                .addAllItems(convertItems(source))
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

    private List<OrderItemDto> convertItems(final OrderEntity source) {
        return source.getItems().stream()
                .map(orderItemEntityToOrderItemDtoConverter::convert)
                .collect(Collectors.toList());
    }
}

package no.acntech.order.converter;

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
        return OrderDto.builder()
                .orderId(source.getOrderId())
                .customerId(source.getCustomerId())
                .name(source.getName())
                .description(source.getDescription())
                .status(source.getStatus())
                .items(convertItems(source))
                .created(source.getCreated())
                .modified(source.getModified())
                .build();
    }

    private List<OrderItemDto> convertItems(final OrderEntity source) {
        return source.getItems().stream()
                .map(orderItemEntityToOrderItemDtoConverter::convert)
                .collect(Collectors.toList());
    }
}

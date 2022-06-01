package no.acntech.order.converter;

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
        return OrderItemDto.builder()
                .itemId(source.getItemId())
                .orderId(source.getParent().getOrderId())
                .productId(source.getProductId())
                .reservationId(source.getReservationId())
                .quantity(source.getQuantity())
                .status(source.getStatus())
                .created(source.getCreated())
                .modified(source.getModified())
                .build();
    }
}

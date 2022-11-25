package no.acntech.order.converter;

import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.OrderItemEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

@Component
public class CreateOrderItemDtoToOrderItemEntityConverter implements Converter<CreateOrderItemDto, OrderItemEntity> {

    @NotNull
    @Override
    public OrderItemEntity convert(@NonNull final CreateOrderItemDto source) {
        return OrderItemEntity.builder()
                .reservationId(source.getReservationId())
                .productId(source.getProductId())
                .quantity(source.getQuantity())
                .status(source.getStatus())
                .build();
    }
}

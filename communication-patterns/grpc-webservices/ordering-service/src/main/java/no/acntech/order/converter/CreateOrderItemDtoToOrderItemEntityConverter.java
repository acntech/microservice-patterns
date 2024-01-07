package no.acntech.order.converter;

import jakarta.validation.constraints.NotNull;
import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.OrderItemEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateOrderItemDtoToOrderItemEntityConverter implements Converter<CreateOrderItemDto, OrderItemEntity> {

    @NotNull
    @Override
    public OrderItemEntity convert(@NonNull final CreateOrderItemDto source) {
        return OrderItemEntity.builder()
                .reservationId(UUID.fromString(source.getReservationId().getValue()))
                .productId(UUID.fromString(source.getProductId().getValue()))
                .quantity(source.getQuantity().getValue())
                .status(source.getStatus())
                .build();
    }
}

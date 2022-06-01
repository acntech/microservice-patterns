package no.acntech.order.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.order.model.OrderEvent;
import no.acntech.reservation.model.CreateReservationDto;

@Component
public class OrderEventToCreateReservationDtoConverter implements Converter<OrderEvent, CreateReservationDto> {

    @Override
    public CreateReservationDto convert(@NonNull final OrderEvent orderEvent) {
        return CreateReservationDto.builder()
                .orderId(orderEvent.getOrderId())
                .productId(orderEvent.getProductId())
                .quantity(orderEvent.getQuantity())
                .build();
    }
}

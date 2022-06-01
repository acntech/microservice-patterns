package no.acntech.order.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.order.model.OrderEvent;
import no.acntech.reservation.model.CancelReservationDto;

@Component
public class OrderEventToCancelReservationDtoConverter implements Converter<OrderEvent, CancelReservationDto> {

    @Override
    public CancelReservationDto convert(@NonNull final OrderEvent orderEvent) {
        return CancelReservationDto.builder()
                .orderId(orderEvent.getOrderId())
                .productId(orderEvent.getProductId())
                .build();
    }
}

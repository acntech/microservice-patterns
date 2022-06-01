package no.acntech.reservation.converter;

import no.acntech.order.model.OrderItemStatus;
import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.reservation.model.ReservationEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventToUpdateOrderItemDtoConverter implements Converter<ReservationEvent, UpdateOrderItemDto> {

    @NonNull
    @Override
    public UpdateOrderItemDto convert(@NonNull final ReservationEvent reservationEvent) {
        return UpdateOrderItemDto.builder()
                .reservationId(reservationEvent.getReservationId())
                .quantity(reservationEvent.getQuantity())
                .status(OrderItemStatus.valueOf(reservationEvent.getStatus().name()))
                .build();
    }
}

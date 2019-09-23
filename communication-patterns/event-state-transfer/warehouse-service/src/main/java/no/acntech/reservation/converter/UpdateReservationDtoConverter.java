package no.acntech.reservation.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderStatus;
import no.acntech.reservation.model.ReservationStatus;
import no.acntech.reservation.model.UpdateReservationDto;

@Component
public class UpdateReservationDtoConverter implements Converter<OrderEvent, UpdateReservationDto> {

    @Override
    public UpdateReservationDto convert(@NonNull final OrderEvent orderEvent) {
        ReservationStatus reservationStatus = convert(orderEvent.getOrderStatus());
        return UpdateReservationDto.builder()
                .orderId(orderEvent.getOrderId())
                .status(reservationStatus)
                .productId(orderEvent.getProductId())
                .quantity(orderEvent.getQuantity())
                .build();
    }

    private ReservationStatus convert(final OrderStatus orderStatus) {
        if (OrderStatus.CONFIRMED.equals(orderStatus)) {
            return ReservationStatus.CONFIRMED;
        } else {
            return null;
        }
    }
}

package no.acntech.order.converter;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderStatus;
import no.acntech.reservation.model.ReservationStatus;
import no.acntech.reservation.model.UpdateReservationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderEventToUpdateReservationDtoConverter implements Converter<OrderEvent, UpdateReservationDto> {

    @Override
    public UpdateReservationDto convert(@NonNull final OrderEvent orderEvent) {
        return UpdateReservationDto.builder()
                .orderId(orderEvent.getOrderId())
                .status(convert(orderEvent.getOrderStatus()))
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

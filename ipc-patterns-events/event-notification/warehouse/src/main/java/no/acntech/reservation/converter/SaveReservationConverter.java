package no.acntech.reservation.converter;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import no.acntech.reservation.model.SaveReservation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class SaveReservationConverter implements Converter<OrderEvent, SaveReservation> {

    @Override
    public SaveReservation convert(@Valid @NotNull final OrderEvent orderEvent) {
        if (orderEvent == null || OrderEventType.ORDER_UPDATED != orderEvent.getType()) {
            return null;
        } else {
            return SaveReservation.builder()
                    .orderId(orderEvent.getOrderId())
                    .productId(orderEvent.getProductId())
                    .quantity(orderEvent.getQuantity())
                    .build();
        }
    }
}

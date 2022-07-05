package no.acntech.reservation.converter;

import no.acntech.order.model.OrderItemStatus;
import no.acntech.order.model.UpdateOrderItemReservationDto;
import no.acntech.reservation.model.ReservationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ReservationDtoToUpdateOrderReservationDtoConverter implements Converter<ReservationDto, UpdateOrderItemReservationDto> {

    @NonNull
    @Override
    public UpdateOrderItemReservationDto convert(@NonNull final ReservationDto source) {
        return UpdateOrderItemReservationDto.builder()
                .orderId(source.getOrderId())
                .productId(source.getProductId())
                .status(OrderItemStatus.valueOf(source.getStatus().name()))
                .build();
    }
}

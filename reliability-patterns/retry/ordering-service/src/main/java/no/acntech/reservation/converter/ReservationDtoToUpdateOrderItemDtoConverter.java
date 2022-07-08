package no.acntech.reservation.converter;

import no.acntech.order.model.OrderItemStatus;
import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.reservation.model.ReservationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ReservationDtoToUpdateOrderItemDtoConverter implements Converter<ReservationDto, UpdateOrderItemDto> {

    @NonNull
    @Override
    public UpdateOrderItemDto convert(@NonNull final ReservationDto source) {
        return UpdateOrderItemDto.builder()
                .quantity(source.getQuantity())
                .status(OrderItemStatus.valueOf(source.getStatus().name()))
                .build();
    }
}

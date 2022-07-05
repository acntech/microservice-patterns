package no.acntech.order.converter;

import no.acntech.order.model.OrderItemDto;
import no.acntech.reservation.model.DeleteReservationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDtoToDeleteReservationDtoConverter implements Converter<OrderItemDto, DeleteReservationDto> {

    @NonNull
    @Override
    public DeleteReservationDto convert(@NonNull final OrderItemDto source) {
        return DeleteReservationDto.builder()
                .reservationId(source.getReservationId())
                .build();
    }
}

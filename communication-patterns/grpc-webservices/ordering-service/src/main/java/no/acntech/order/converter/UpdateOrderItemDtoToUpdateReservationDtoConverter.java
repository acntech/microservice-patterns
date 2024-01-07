package no.acntech.order.converter;

import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderItemDtoToUpdateReservationDtoConverter implements Converter<UpdateOrderItemDto, UpdateReservationDto> {

    @NonNull
    @Override
    public UpdateReservationDto convert(@NonNull final UpdateOrderItemDto source) {
        return UpdateReservationDto.newBuilder()
                .setQuantity(source.getQuantity())
                .build();
    }
}

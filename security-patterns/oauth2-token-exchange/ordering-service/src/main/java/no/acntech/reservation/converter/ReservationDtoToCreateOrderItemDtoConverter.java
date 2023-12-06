package no.acntech.reservation.converter;

import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.reservation.model.ReservationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

@Component
public class ReservationDtoToCreateOrderItemDtoConverter implements Converter<ReservationDto, CreateOrderItemDto> {

    @NotNull
    @Override
    public CreateOrderItemDto convert(@NonNull final ReservationDto source) {
        return null;
    }
}

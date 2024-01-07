package no.acntech.reservation.converter;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.CreateReservationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateReservationDtoToCreateReservationRequestConverter implements Converter<CreateReservationDto, CreateReservationRequest> {

    @NonNull
    @Override
    public CreateReservationRequest convert(@NonNull final CreateReservationDto source) {
        return CreateReservationRequest.newBuilder()
                .setBody(source)
                .build();
    }
}

package no.acntech.reservation.converter;

import no.acntech.reservation.model.FindReservationsRequest;
import no.acntech.reservation.model.ReservationQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindReservationsRequestToReservationQueryConverter implements Converter<FindReservationsRequest, ReservationQuery> {

    @NonNull
    @Override
    public ReservationQuery convert(@NonNull final FindReservationsRequest source) {
        return ReservationQuery.build()
                .orderId(source.getHeader().hasOrderId() ? UUID.fromString(source.getHeader().getOrderId().getValue()) : null)
                .build();
    }
}

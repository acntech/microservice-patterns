package no.acntech.reservation.converter;

import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ReservationDtoConverter implements Converter<Reservation, ReservationDto> {

    @Override
    public ReservationDto convert(@NonNull final Reservation reservation) {
        return ReservationDto.builder()
                .reservationId(reservation.getReservationId())
                .orderId(reservation.getOrderId())
                .productId(reservation.getProduct() != null ? reservation.getProduct().getProductId() : null)
                .quantity(reservation.getQuantity())
                .status(reservation.getStatus())
                .created(reservation.getCreated())
                .modified(reservation.getModified())
                .build();
    }
}

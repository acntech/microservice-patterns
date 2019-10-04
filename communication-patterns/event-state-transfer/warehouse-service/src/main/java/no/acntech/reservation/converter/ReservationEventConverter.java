package no.acntech.reservation.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationEventType;
import no.acntech.reservation.model.ReservationStatus;

@Component
public class ReservationEventConverter implements Converter<Reservation, ReservationEvent> {

    @Override
    public ReservationEvent convert(@NonNull final Reservation reservation) {
        ReservationEventType reservationEventType = convert(reservation.getStatus());
        return ReservationEvent.builder()
                .eventType(reservationEventType)
                .reservationId(reservation.getReservationId())
                .status(reservation.getStatus())
                .orderId(reservation.getOrderId())
                .productId(reservation.getProduct().getProductId())
                .quantity(reservation.getQuantity())
                .build();
    }

    private ReservationEventType convert(@NonNull final ReservationStatus reservationStatus) {
        switch (reservationStatus) {
            case RESERVED:
                return ReservationEventType.RESERVATION_CREATED;
            case CONFIRMED:
                return ReservationEventType.RESERVATION_CONFIRMED;
            case REJECTED:
                return ReservationEventType.RESERVATION_REJECTED;
            case CANCELED:
                return ReservationEventType.RESERVATION_CANCELED;
            default:
                return ReservationEventType.RESERVATION_FAILED;
        }
    }
}

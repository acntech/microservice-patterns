package no.acntech.reservation.converter;

import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationEventType;
import no.acntech.reservation.model.ReservationStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ReservationDtoToReservationEventConverter implements Converter<ReservationDto, ReservationEvent> {

    @Override
    public ReservationEvent convert(@NonNull final ReservationDto reservationDto) {
        return ReservationEvent.builder()
                .eventType(convert(reservationDto.getStatus()))
                .reservationId(reservationDto.getReservationId())
                .status(reservationDto.getStatus())
                .orderId(reservationDto.getOrderId())
                .productId(reservationDto.getProductId())
                .quantity(reservationDto.getQuantity())
                .build();
    }

    private ReservationEventType convert(@NonNull final ReservationStatus reservationStatus) {
        return switch (reservationStatus) {
            case RESERVED -> ReservationEventType.RESERVATION_CREATED;
            case CONFIRMED -> ReservationEventType.RESERVATION_CONFIRMED;
            case REJECTED -> ReservationEventType.RESERVATION_REJECTED;
            case CANCELED -> ReservationEventType.RESERVATION_CANCELED;
            default -> ReservationEventType.RESERVATION_FAILED;
        };
    }
}

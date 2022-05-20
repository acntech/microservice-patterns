package no.acntech.reservation.converter;

import no.acntech.reservation.model.ReservationEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.reservation.model.ReservationDto;

@Component
public class ReservationEntityToReservationDtoConverter implements Converter<ReservationEntity, ReservationDto> {

    @NonNull
    @Override
    public ReservationDto convert(@NonNull final ReservationEntity reservationEntity) {
        return ReservationDto.builder()
                .reservationId(reservationEntity.getReservationId())
                .orderId(reservationEntity.getOrderId())
                .productId(reservationEntity.getProduct() != null ? reservationEntity.getProduct().getProductId() : null)
                .quantity(reservationEntity.getQuantity())
                .status(reservationEntity.getStatus())
                .created(reservationEntity.getCreated())
                .modified(reservationEntity.getModified())
                .build();
    }
}

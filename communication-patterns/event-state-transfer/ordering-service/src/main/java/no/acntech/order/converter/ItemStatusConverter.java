package no.acntech.order.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.order.model.ItemStatus;
import no.acntech.reservation.model.ReservationStatus;

@Component
public class ItemStatusConverter implements Converter<ReservationStatus, ItemStatus> {

    @Override
    public ItemStatus convert(@NonNull ReservationStatus reservationStatus) {
        try {
            return ItemStatus.valueOf(reservationStatus.name());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

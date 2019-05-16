package no.acntech.reservation.service;

import no.acntech.order.model.Item;
import no.acntech.order.model.ItemStatus;
import no.acntech.order.repository.ItemRepository;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRestConsumer reservationRestConsumer;
    private final ItemRepository itemRepository;

    public ReservationService(final ReservationRestConsumer reservationRestConsumer,
                              final ItemRepository itemRepository) {
        this.reservationRestConsumer = reservationRestConsumer;
        this.itemRepository = itemRepository;
    }

    public void receiveReservationEvent(final ReservationEvent reservationEvent) {
        final UUID reservationId = reservationEvent.getReservationId();

        LOGGER.debug("Retrieving reservation for reservation-id {}", reservationId);
        final Optional<ReservationDto> reservationOptional = reservationRestConsumer.get(reservationId);

        if (reservationOptional.isPresent()) {
            ReservationDto reservation = reservationOptional.get();

            processReservation(reservation);
        } else {
            LOGGER.error("Reservation with reservation-id {} could not be found", reservationId);
        }
    }

    private void processReservation(final ReservationDto reservationDto) {
        final UUID reservationId = reservationDto.getReservationId();
        final Long quantity = reservationDto.getQuantity();
        final ReservationStatus reservationStatus = reservationDto.getStatus();
        LOGGER.debug("Processing reservation for reservation-id {}", reservationId);

        Optional<Item> exitingItem = itemRepository.findByReservationId(reservationId);

        if (exitingItem.isPresent()) {
            Item item = exitingItem.get();
            ItemStatus status = ItemStatus.valueOf(reservationStatus.name());
            item.setStatus(status);
            item.setQuantity(quantity);

            itemRepository.save(item);
        } else {
            LOGGER.error("Could not find order item for reservation-id {}", reservationId);
        }
    }
}

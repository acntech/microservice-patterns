package no.acntech.reservation.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.ItemStatus;
import no.acntech.order.service.OrderFacadeService;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationStatus;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRestConsumer reservationRestConsumer;
    private final OrderFacadeService orderFacadeService;

    public ReservationService(final ReservationRestConsumer reservationRestConsumer,
                              final OrderFacadeService orderFacadeService) {
        this.reservationRestConsumer = reservationRestConsumer;
        this.orderFacadeService = orderFacadeService;
    }

    public void receiveReservationEvent(final ReservationEvent reservationEvent) {
        UUID reservationId = reservationEvent.getReservationId();

        LOGGER.debug("Retrieving reservation for reservation-id {}", reservationId);

        try {
            Optional<ReservationDto> reservationOptional = reservationRestConsumer.get(reservationId);

            if (reservationOptional.isPresent()) {
                ReservationDto reservation = reservationOptional.get();

                processReservation(reservation);
            } else {
                LOGGER.error("Reservation with reservation-id {} could not be found", reservationId);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing reservation", e);
        }
    }

    private void processReservation(final ReservationDto reservationDto) {
        UUID reservationId = reservationDto.getReservationId();
        Long quantity = reservationDto.getQuantity();
        ReservationStatus reservationStatus = reservationDto.getStatus();

        LOGGER.debug("Processing reservation for reservation-id {}", reservationId);

        ItemStatus status = ItemStatus.valueOf(reservationStatus.name());

        orderFacadeService.updateItemReservation(reservationId, quantity, status);
    }
}

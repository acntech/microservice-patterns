package no.acntech.reservation.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.Item;
import no.acntech.order.model.ItemStatus;
import no.acntech.order.model.Order;
import no.acntech.order.model.UpdateItemDto;
import no.acntech.order.service.OrderService;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationStatus;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRestConsumer reservationRestConsumer;
    private final OrderService orderService;

    public ReservationService(final ReservationRestConsumer reservationRestConsumer,
                              final OrderService orderService) {
        this.reservationRestConsumer = reservationRestConsumer;
        this.orderService = orderService;
    }

    public void receiveReservationEvent(final ReservationEvent reservationEvent) {
        final UUID reservationId = reservationEvent.getReservationId();

        LOGGER.debug("Retrieving reservation for reservation-id {}", reservationId);
        final Optional<ReservationDto> reservationOptional = reservationRestConsumer.get(reservationId);

        if (reservationOptional.isPresent()) {
            ReservationDto reservation = reservationOptional.get();

            switch (reservation.getStatus()) {
                case CONFIRMED:
                case REJECTED:
                    processReservation(reservation);
                    break;
                case CANCELED:
                    LOGGER.debug("Reservation with reservation-id {} has status canceled, and will be ignored", reservationId);
                    break;
                default:
                    LOGGER.error("Reservation with reservation-id {} has invalid status", reservationId);
            }
        } else {
            LOGGER.error("Reservation with reservation-id {} could not be found", reservationId);
        }
    }

    private void processReservation(final ReservationDto reservationDto) {
        final UUID reservationId = reservationDto.getReservationId();
        final UUID orderId = reservationDto.getOrderId();
        final UUID productId = reservationDto.getProductId();
        final ReservationStatus reservationStatus = reservationDto.getStatus();
        LOGGER.debug("Processing reservation confirmation {}", reservationId);

        final Order order = orderService.getOrder(orderId);
        Optional<Item> exitingItem = order.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
        if (exitingItem.isPresent()) {
            final ItemStatus status = ItemStatus.valueOf(reservationStatus.name());
            final UpdateItemDto updateItem = UpdateItemDto.builder()
                    .productId(productId)
                    .status(status)
                    .build();
            orderService.updateItem(orderId, updateItem);
        } else {
            LOGGER.error("Could not find order item for product-id {} and order-id {}", productId, orderId);
        }
    }
}

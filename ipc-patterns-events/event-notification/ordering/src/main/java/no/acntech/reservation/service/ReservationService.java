package no.acntech.reservation.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.Item;
import no.acntech.order.model.ItemStatus;
import no.acntech.order.model.Order;
import no.acntech.order.model.UpdateItem;
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
        LOGGER.debug("Received reservation event with ID {}", reservationId);

        LOGGER.debug("Retrieving reservation for ID {}", reservationId);
        final ReservationDto reservationDto = reservationRestConsumer.get(reservationId);

        switch (reservationDto.getStatus()) {
            case CONFIRMED:
            case REJECTED:
                processReservation(reservationDto);
                break;
            case CANCELED:
                LOGGER.debug("Received reservation canceled event. This event will be ignored.");
                break;
            default:
                LOGGER.warn("Received reservation event with invalid status");
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
            final ItemStatus itemStatus = ItemStatus.valueOf(reservationStatus.name());
            final UpdateItem updateItem = UpdateItem.builder()
                    .orderId(orderId)
                    .productId(productId)
                    .status(itemStatus)
                    .build();
            orderService.updateItem(updateItem);
        } else {
            LOGGER.error("Could not find order item for product-id {} and order-id {}", productId, orderId);
        }
    }
}

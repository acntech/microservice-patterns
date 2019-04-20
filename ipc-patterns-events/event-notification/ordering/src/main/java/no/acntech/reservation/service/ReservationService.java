package no.acntech.reservation.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.Item;
import no.acntech.order.model.ItemStatus;
import no.acntech.order.model.Order;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationStatus;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRestConsumer reservationRestConsumer;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public ReservationService(final ReservationRestConsumer reservationRestConsumer,
                              final OrderRepository orderRepository,
                              final ItemRepository itemRepository) {
        this.reservationRestConsumer = reservationRestConsumer;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
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
                case CANCELED:
                    processReservation(reservation);
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
        final Long quantity = reservationDto.getQuantity();
        final ReservationStatus reservationStatus = reservationDto.getStatus();
        LOGGER.debug("Processing reservation confirmation {}", reservationId);

        Optional<Order> existingOrder = orderRepository.findByOrderId(orderId);

        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();

            Optional<Item> exitingItem = itemRepository.findByOrderIdAndProductId(order.getId(), productId);

            if (exitingItem.isPresent()) {
                Item item = exitingItem.get();
                ItemStatus status = ItemStatus.valueOf(reservationStatus.name());
                item.setStatus(status);
                item.setQuantity(quantity);

                itemRepository.save(item);
            } else {
                LOGGER.error("Could not find order item for product-id {} and order-id {}", productId, orderId);
            }
        } else {
            LOGGER.error("Could not find order for order-id {}", orderId);

        }
    }
}

package no.acntech.reservation.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.Item;
import no.acntech.order.model.ItemStatus;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderStatus;
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
        UUID orderId = reservationDto.getOrderId();
        UUID reservationId = reservationDto.getReservationId();
        Long quantity = reservationDto.getQuantity();
        ReservationStatus reservationStatus = reservationDto.getStatus();

        LOGGER.debug("Processing reservation for reservation-id {}", reservationId);

        Optional<Item> exitingItem = itemRepository.findByReservationId(reservationId);

        if (exitingItem.isPresent()) {
            Item item = exitingItem.get();
            ItemStatus status = ItemStatus.valueOf(reservationStatus.name());
            item.setStatus(status);
            item.setQuantity(quantity);

            LOGGER.debug("Updating order item status to {} for order-id {} and reservation-id {}", status.name(), orderId, reservationId);

            itemRepository.save(item);

            Optional<Order> existingOrder = orderRepository.findByOrderId(orderId);
            if (existingOrder.isPresent()) {
                Order order = existingOrder.get();

                boolean allItemsConfirmed = order.getItems().stream()
                        .map(Item::getStatus)
                        .filter(itemStatus -> !ItemStatus.CANCELED.equals(itemStatus))
                        .allMatch(ItemStatus.CONFIRMED::equals);

                if (allItemsConfirmed) {
                    LOGGER.debug("Updating order status to {} for order-id {}", OrderStatus.CONFIRMED.name(), orderId);

                    order.setStatus(OrderStatus.CONFIRMED);
                    orderRepository.save(order);
                }
            } else {
                LOGGER.error("Could not find order for order-id {}", orderId);
            }
        } else {
            LOGGER.error("Could not find order item for reservation-id {}", reservationId);
        }
    }
}

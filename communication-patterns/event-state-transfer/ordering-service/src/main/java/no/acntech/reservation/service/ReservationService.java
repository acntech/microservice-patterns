package no.acntech.reservation.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import no.acntech.order.model.Item;
import no.acntech.order.model.ItemStatus;
import no.acntech.order.model.Order;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationEventType;
import no.acntech.reservation.model.ReservationStatus;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public ReservationService(final ConversionService conversionService,
                              final OrderRepository orderRepository,
                              final ItemRepository itemRepository) {
        this.conversionService = conversionService;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    public void receiveReservationEvent(final ReservationEvent reservationEvent) {
        try {
            processReservationEvent(reservationEvent);
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing reservation event", e);
        }
    }

    private void processReservationEvent(final ReservationEvent reservationEvent) {
        ReservationEventType eventType = reservationEvent.getEventType();
        UUID reservationId = reservationEvent.getReservationId();

        LOGGER.debug("Processing reservation event for reservation-id {}", reservationId);

        switch (eventType) {
            case RESERVATION_CREATED:
            case RESERVATION_CONFIRMED:
            case RESERVATION_REJECTED:
            case RESERVATION_FAILED:
                processReservation(reservationEvent);
                break;
            default:
                LOGGER.debug("Ignoring reservation event with type {} for reservation-id {}", eventType, reservationId);
        }
    }

    private void processReservation(final ReservationEvent reservationEvent) {
        UUID orderId = reservationEvent.getOrderId();
        UUID productId = reservationEvent.getProductId();

        Optional<Order> existingOrder = orderRepository.findByOrderId(orderId);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();

            Optional<Item> existingItem = itemRepository.findByOrderIdAndProductId(order.getId(), productId);
            if (existingItem.isPresent()) {
                Item item = existingItem.get();

                UUID reservationId = reservationEvent.getReservationId();
                ReservationStatus reservationStatus = reservationEvent.getStatus();
                ItemStatus itemStatus = conversionService.convert(reservationStatus, ItemStatus.class);

                item.setReservationId(reservationId);
                if (itemStatus != null) {
                    item.setStatus(itemStatus);
                }

                itemRepository.save(item);
            } else {
                LOGGER.error("Could not find order item for product-id {}", productId);
            }
        } else {
            LOGGER.error("Could not find order for order-id {}", orderId);
        }
    }
}

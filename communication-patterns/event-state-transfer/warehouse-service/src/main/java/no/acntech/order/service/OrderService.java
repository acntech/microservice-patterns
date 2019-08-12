package no.acntech.order.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import no.acntech.reservation.service.ReservationService;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final ReservationService reservationService;

    public OrderService(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void receiveOrderEvent(final OrderEvent orderEvent) {
        if (orderEvent == null) {
            LOGGER.error("Received order event which was null");
        } else {
            try {
                processReservationEvent(orderEvent);
            } catch (Exception e) {
                LOGGER.error("Error occurred while processing order event", e);
            }
        }
    }

    private void processReservationEvent(final OrderEvent orderEvent) {
        OrderEventType eventType = orderEvent.getEventType();
        UUID orderId = orderEvent.getOrderId();

        LOGGER.debug("Processing order event for order-id {}", orderId);

        switch (eventType) {
            case ORDER_ITEM_ADDED:
                processOrderItemAdded(orderEvent);
                break;
            case ORDER_ITEM_UPDATED:
                processOrderItemUpdated(orderEvent);
                break;
            case ORDER_ITEM_CANCELED:
                processOrderItemCanceled(orderEvent);
                break;
            default:
                LOGGER.debug("Ignoring order event with type {} for order-id {}", eventType, orderId);
        }
    }

    private void processOrderItemAdded(final OrderEvent orderEvent) {

    }

    private void processOrderItemUpdated(final OrderEvent orderEvent) {

    }

    private void processOrderItemCanceled(final OrderEvent orderEvent) {

    }
}

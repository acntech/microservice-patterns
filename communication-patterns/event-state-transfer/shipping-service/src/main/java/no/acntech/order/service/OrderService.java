package no.acntech.order.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import no.acntech.order.model.OrderStatus;
import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.service.ShipmentService;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final ConversionService conversionService;
    private final ShipmentService shipmentService;

    public OrderService(final ConversionService conversionService,
                        final ShipmentService shipmentService) {
        this.conversionService = conversionService;
        this.shipmentService = shipmentService;
    }

    public void receiveOrderEvent(final OrderEvent orderEvent) {
        try {
            processReservationEvent(orderEvent);
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order event", e);
        }
    }

    private void processReservationEvent(final OrderEvent orderEvent) {
        OrderEventType eventType = orderEvent.getEventType();
        UUID orderId = orderEvent.getOrderId();
        OrderStatus orderStatus = orderEvent.getOrderStatus();

        LOGGER.debug("Processing order event for order-id {}", orderId);

        if (OrderEventType.ORDER_UPDATED.equals(eventType) && OrderStatus.CONFIRMED.equals(orderStatus)) {
            processOrderUpdated(orderEvent);
        } else {
            LOGGER.debug("Ignoring order event with type {} for order-id {}", eventType, orderId);
        }
    }

    private void processOrderUpdated(final OrderEvent orderEvent) {
        CreateShipmentDto createShipment = conversionService.convert(orderEvent, CreateShipmentDto.class);
        if (createShipment != null) {
            shipmentService.createShipment(createShipment);
        } else {
            LOGGER.error("Could not convert order event to update reservation DTO");
        }
    }
}

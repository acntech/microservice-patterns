package no.acntech.order.service;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import no.acntech.order.model.OrderStatus;
import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    public void processOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Processing OrderEvent with order-id {}", orderEvent.getOrderId());

        try {
            processReservationEvent(orderEvent);
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order event", e);
        }
    }

    private void processReservationEvent(final OrderEvent orderEvent) {
        if (orderEvent.getEventType() == OrderEventType.ORDER_UPDATED && orderEvent.getOrderStatus() == OrderStatus.CONFIRMED) {
            var createShipmentDto = conversionService.convert(orderEvent, CreateShipmentDto.class);
            Assert.notNull(createShipmentDto, "Failed to convert OrderEvent to CreateShipmentDto");
            shipmentService.createShipment(createShipmentDto);
        } else {
            LOGGER.debug("Ignoring order event with type {} for order-id {}", orderEvent.getEventType(), orderEvent.getOrderId());
        }
    }
}

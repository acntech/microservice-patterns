package no.acntech.order.service;

import no.acntech.order.consumer.OrderRestConsumer;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;
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
    private final OrderRestConsumer orderRestConsumer;
    private final ShipmentService shipmentService;

    public OrderService(final ConversionService conversionService,
                        final OrderRestConsumer orderRestConsumer,
                        final ShipmentService shipmentService) {
        this.conversionService = conversionService;
        this.orderRestConsumer = orderRestConsumer;
        this.shipmentService = shipmentService;
    }

    @SuppressWarnings("Duplicates")
    public void processOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Processing OrderEvent with order-id {}", orderEvent.getOrderId());

        try {
            LOGGER.debug("Fetching OrderDto for order-id {}", orderEvent.getOrderId());
            final var orderOptional = orderRestConsumer.get(orderEvent.getOrderId());
            if (orderOptional.isPresent()) {
                final var order = orderOptional.get();
                processOrder(order);
            } else {
                LOGGER.error("OrderDto with order-id {} could not be found", orderEvent.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing OrderEvent with order-id " + orderEvent.getOrderId(), e);
        }
    }

    private void processOrder(final OrderDto orderDto) {
        LOGGER.debug("Processing OrderDto with order-id {}", orderDto.getOrderId());

        if (orderDto.getStatus() == OrderStatus.CONFIRMED) {
            final var createShipmentDto = conversionService.convert(orderDto, CreateShipmentDto.class);
            Assert.notNull(createShipmentDto, "Failed to convert OrderDto to CreateShipmentDto");
            shipmentService.createShipment(createShipmentDto);
        } else {
            LOGGER.debug("Ignoring OrderDto for order-id {} and status {}", orderDto.getOrderId(), orderDto.getStatus());
        }
    }
}

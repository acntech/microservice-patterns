package no.acntech.order.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.consumer.OrderRestConsumer;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRestConsumer orderRestConsumer;

    public OrderService(final OrderRestConsumer orderRestConsumer) {
        this.orderRestConsumer = orderRestConsumer;
    }

    public void receiveOrderEvent(final OrderEvent orderEvent) {
        final UUID orderId = orderEvent.getOrderId();

        LOGGER.debug("Retrieving order for order-id {}", orderId);

        try {
            Optional<OrderDto> orderOptional = orderRestConsumer.get(orderId);

            if (orderOptional.isPresent()) {
                OrderDto order = orderOptional.get();

                processOrder(order);
            } else {
                LOGGER.error("Order with order-id {} could not be found", orderId);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order", e);
        }
    }

    private void processOrder(final OrderDto order) {
        final UUID orderId = order.getOrderId();

        LOGGER.debug("Processing order for order-id {}", orderId);
    }
}

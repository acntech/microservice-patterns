package no.acntech.order.service;

import no.acntech.order.consumer.OrderRestConsumer;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRestConsumer orderRestConsumer;

    public OrderService(final OrderRestConsumer orderRestConsumer) {
        this.orderRestConsumer = orderRestConsumer;
    }

    @SuppressWarnings("Duplicates")
    public void receiveOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Fetching order for order-id {}", orderEvent.getOrderId());

        try {
            final var optionalOrderDto = orderRestConsumer.get(orderEvent.getOrderId());
            if (optionalOrderDto.isPresent()) {
                final var orderDto = optionalOrderDto.get();
                processOrder(orderDto);
            } else {
                LOGGER.error("Order with order-id {} could not be found", orderEvent.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order", e);
        }
    }

    private void processOrder(final OrderDto orderDto) {
        LOGGER.debug("Processing orderDto for orderDto-id {}", orderDto.getOrderId());
        LOGGER.debug("Ignoring orderDto for orderDto-id {} and status {}", orderDto.getOrderId(), orderDto.getStatus());
    }
}

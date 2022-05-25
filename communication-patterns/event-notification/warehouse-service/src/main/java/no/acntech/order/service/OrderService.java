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
    public void processOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Processing OrderEvent with order-id {}", orderEvent.getOrderId());

        try {
            LOGGER.debug("Fetching OrderDto with order-id {}", orderEvent.getOrderId());
            final var optionalOrderDto = orderRestConsumer.get(orderEvent.getOrderId());
            if (optionalOrderDto.isPresent()) {
                final var orderDto = optionalOrderDto.get();
                processOrder(orderDto);
            } else {
                LOGGER.error("OrderDto with order-id {} could not be found", orderEvent.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing OrderDto with order-id " + orderEvent.getOrderId(), e);
        }
    }

    private void processOrder(final OrderDto orderDto) {
        LOGGER.debug("Processing OrderDto for order-id {}", orderDto.getOrderId());
        LOGGER.debug("Ignoring OrderDto for order-id {} and status {}", orderDto.getOrderId(), orderDto.getStatus());
    }
}

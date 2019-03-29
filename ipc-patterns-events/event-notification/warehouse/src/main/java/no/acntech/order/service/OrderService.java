package no.acntech.order.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.OrderEvent;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public void receiveOrderEvent(final OrderEvent orderEvent) {
        final UUID orderId = orderEvent.getOrderId();
        LOGGER.debug("Received order event with ID {}", orderId);
    }
}

package no.acntech.order.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.consumer.OrderRestConsumer;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;
import no.acntech.reservation.service.ReservationService;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRestConsumer orderRestConsumer;
    private final ReservationService reservationService;

    public OrderService(final OrderRestConsumer orderRestConsumer,
                        final ReservationService reservationService) {
        this.orderRestConsumer = orderRestConsumer;
        this.reservationService = reservationService;
    }

    public void receiveOrderEvent(final OrderEvent orderEvent) {
        final UUID orderId = orderEvent.getOrderId();
        OrderDto orderDto = orderRestConsumer.get(orderId);
    }
}

package no.acntech.order.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import no.acntech.order.model.ItemStatus;
import no.acntech.order.producer.OrderEventProducer;

@Service
public class OrderFacadeService {

    private final OrderService orderService;
    private final OrderEventProducer orderEventProducer;

    public OrderFacadeService(final OrderService orderService,
                              final OrderEventProducer orderEventProducer) {
        this.orderService = orderService;
        this.orderEventProducer = orderEventProducer;
    }

    public void updateItemReservation(UUID reservationId, Long quantity, ItemStatus status) {
        Optional<UUID> orderId = orderService.updateItemReservation(reservationId, quantity, status);
        orderId.ifPresent(orderEventProducer::publish);
    }
}

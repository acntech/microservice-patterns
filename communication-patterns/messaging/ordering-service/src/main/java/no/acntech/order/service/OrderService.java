package no.acntech.order.service;

import javax.transaction.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.shipping.producer.ShippingMessageProducer;
import no.acntech.reservation.producer.ReservationMessageProducer;
import no.acntech.order.model.Order;
import no.acntech.order.model.Orderstatus;
import no.acntech.order.repository.OrderRepository;
import no.acntech.reservation.model.InventoryReservationConfirmationMessage;

@Service
@Transactional
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ReservationMessageProducer warehouseMessageProducer;
    private final ShippingMessageProducer shippingMessageProducer;

    public OrderService(final OrderRepository orderRepository,
                        final ReservationMessageProducer warehouseMessageProducer,
                        final ShippingMessageProducer shippingMessageProducer) {
        this.orderRepository = orderRepository;
        this.warehouseMessageProducer = warehouseMessageProducer;
        this.shippingMessageProducer = shippingMessageProducer;
    }

    /**
     * Publishes a message asynchronously to warehouse when order is submitted.
     * Message is submitted in the same transaction
     */
    public Order submit(Order order) {
        order = orderRepository.save(order);
        order.setOrderstatus(Orderstatus.WAREHOUSE_RESERVATION_PENDING);

        warehouseMessageProducer.reserve(order);
        LOGGER.info("Reservation sent to warehouse for orderId={}", order.getId());

        return order;
    }

    public void warehouseReservationConfirmed(InventoryReservationConfirmationMessage message) {
        Order order = orderRepository.findById(message.getOrderId())
                .orElseThrow(() -> new IllegalStateException("Received reservation confirmation for orderId=" + message.getOrderId() + " which doesn't exist"));

        if (message.isReserved()) {
            LOGGER.info("Warehouse reservation confirmed for orderId={}, shipping!", order.getId());
            order.setOrderstatus(Orderstatus.SHIPPED);
            shippingMessageProducer.ship(order.getId(), message.getReservationId());
        } else {
            LOGGER.info("Warehouse reservation failed for orderId={}, errorMessage={}", order.getId(), message.getErrorMessage());
            order.setOrderstatus(Orderstatus.WAREHOUSE_RESERVATION_FAILED);
        }
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}

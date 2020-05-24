package no.acntech.order.service;

import javax.transaction.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.messaging.types.warehouse.InventoryReservationConfirmationMessage;
import no.acntech.order.entity.Order;
import no.acntech.order.entity.Orderstatus;
import no.acntech.order.messaging.producers.ShippingMessageProducer;
import no.acntech.order.messaging.producers.WarehouseMessageProducer;
import no.acntech.order.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final WarehouseMessageProducer warehouseMessageProducer;
    private final ShippingMessageProducer shippingMessageProducer;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        WarehouseMessageProducer warehouseMessageProducer,
                        ShippingMessageProducer shippingMessageProducer) {
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

    public void warehouseReservationConfirmed(InventoryReservationConfirmationMessage reservationConfirmation) {
        Order order = orderRepository.findById(reservationConfirmation.getOrderId())
                .orElseThrow(() -> new IllegalStateException("Received reservation confirmation for orderId=" + reservationConfirmation.getOrderId() + " which doesn't exist"));

        if (reservationConfirmation.isReserved()) {
            LOGGER.info("Warehouse reservation confirmed for orderId={}, shipping!", order.getId());
            order.setOrderstatus(Orderstatus.SHIPPED);
            shippingMessageProducer.ship(order.getId(), reservationConfirmation.getReservationId());
        } else {
            LOGGER.info("Warehouse reservation failed for orderId={}, errorMessage={}", order.getId(), reservationConfirmation.getErrorMessage());
            order.setOrderstatus(Orderstatus.WAREHOUSE_RESERVATION_FAILED);
        }
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}

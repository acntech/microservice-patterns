package no.acntech.order.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderId(UUID orderId);

    List<Order> findAllByCustomerId(UUID customerId);

    List<Order> findAllByCustomerIdAndStatus(UUID customerId, Order.Status status);

    List<Order> findAllByStatus(Order.Status status);
}

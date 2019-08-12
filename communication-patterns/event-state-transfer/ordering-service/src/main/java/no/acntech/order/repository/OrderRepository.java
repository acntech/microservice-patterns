package no.acntech.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.order.model.Order;
import no.acntech.order.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(UUID orderId);

    List<Order> findAllByCustomerId(UUID customerId);

    List<Order> findAllByCustomerIdAndStatus(UUID customerId, OrderStatus status);

    List<Order> findAllByStatus(OrderStatus status);
}

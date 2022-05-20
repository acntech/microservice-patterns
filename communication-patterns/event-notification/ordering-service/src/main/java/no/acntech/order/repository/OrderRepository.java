package no.acntech.order.repository;

import no.acntech.order.model.OrderEntity;
import no.acntech.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(UUID orderId);

    List<OrderEntity> findAllByCustomerId(UUID customerId);

    List<OrderEntity> findAllByCustomerIdAndStatus(UUID customerId, OrderStatus status);

    List<OrderEntity> findAllByStatus(OrderStatus status);
}

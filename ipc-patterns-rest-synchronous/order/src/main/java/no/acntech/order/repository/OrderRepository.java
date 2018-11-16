package no.acntech.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import no.acntech.order.entity.Order;
import no.acntech.order.entity.Orderstatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderstatusLike(Orderstatus registerStatus);
}

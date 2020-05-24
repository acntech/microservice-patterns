package no.acntech.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.acntech.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

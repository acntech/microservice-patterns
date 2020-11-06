package no.acntech.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.acntech.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

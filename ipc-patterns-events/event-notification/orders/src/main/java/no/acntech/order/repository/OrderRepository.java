package no.acntech.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}

package no.acntech.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.order.model.OrderLine;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}

package no.acntech.order.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import no.acntech.order.entity.Order;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {

}

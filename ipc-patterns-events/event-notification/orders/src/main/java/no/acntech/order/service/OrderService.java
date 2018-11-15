package no.acntech.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import no.acntech.order.model.Order;
import no.acntech.order.repository.OrderRepository;

@Service
public class OrderService {


    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findOrders() {
        return orderRepository.findAll();
    }
}

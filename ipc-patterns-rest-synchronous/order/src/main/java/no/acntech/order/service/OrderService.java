package no.acntech.order.service;

import javax.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.order.entity.Order;
import no.acntech.order.entity.Orderstatus;
import no.acntech.order.integration.WarehouseRestClient;
import no.acntech.order.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WarehouseRestClient warehouseRestClient;

    @Autowired
    public OrderService(WarehouseRestClient warehouseRestClient, OrderRepository orderRepository) {
        this.warehouseRestClient = warehouseRestClient;
        this.orderRepository = orderRepository;
    }

    /**
     * Calls warehouse synchronously. If we have a rollback in order after warehouse has responded,
     * we have no means of rolling back warehouse (only local transactions with rest)
     * <p>
     * Calling warehouse synchronously also implies a 100% uptime dependency.
     */
    @Transactional
    public Order create(Order order) {
        // validation, etc...
        order = orderRepository.save(order);
        order.setOrderstatus(Orderstatus.COMPLETED);

        warehouseRestClient.reserve(order);

        return order;
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}

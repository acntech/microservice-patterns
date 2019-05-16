package no.acntech.order.service;

import javax.transaction.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.order.entity.Order;
import no.acntech.order.entity.Orderstatus;
import no.acntech.order.integration.shipping.ShippingRestClient;
import no.acntech.order.repository.OrderRepository;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ShippingRestClient shippingRestClient;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ShippingRestClient shippingRestClient) {
        this.orderRepository = orderRepository;
        this.shippingRestClient = shippingRestClient;
    }

    /**
     * Orchestrates and calls other services synchronously.
     * If we have an error after the services has responded we have no rollback (only local transactions).
     * This also implies 100% uptime requirement on other services.
     */
    @Transactional
    public Order submit(Order order) {
        // validation, etc...
        order = orderRepository.save(order);
        order.setOrderstatus(Orderstatus.COMPLETED);

        String shippingId = shippingRestClient.ship(order);
        order.setShippingId(shippingId);
        LOGGER.info("Order with orderId={} shipped! shippingId={}", order.getId(), shippingId);

        return order;
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}

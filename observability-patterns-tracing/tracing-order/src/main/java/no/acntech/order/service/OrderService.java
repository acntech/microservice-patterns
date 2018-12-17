package no.acntech.order.service;

import brave.ScopedSpan;
import brave.Tracer;

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
    private final Tracer tracer;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ShippingRestClient shippingRestClient,
                        Tracer tracer) {
        this.orderRepository = orderRepository;
        this.shippingRestClient = shippingRestClient;
        this.tracer = tracer;
    }

    @Transactional
    public Order submit(Order order) {
        ScopedSpan scopedSpan = tracer.startScopedSpan("OrderService#submit");
        try {
            LOGGER.debug("Calling OrderRepository#save for order: {}", order.toString());
            order = orderRepository.save(order);

            LOGGER.debug("Calling ShippingRestClient#ship for order: {}", order.toString());
            String shippingId = shippingRestClient.ship(order);

            LOGGER.info("Order with orderId={} shipped! shippingId={}", order.getId(), shippingId);
            order.setShippingId(shippingId);

            LOGGER.debug("Updating Order#orderStatus to {}", Orderstatus.COMPLETED);
            order.setOrderstatus(Orderstatus.COMPLETED);

            LOGGER.debug("Returning from OrderService#submit");
            return order;
        } finally {
            scopedSpan.finish();
        }
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}

package no.acntech.order.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.acntech.order.model.CreateOrder;
import no.acntech.order.model.CreateOrderLine;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderLine;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.repository.OrderLineRepository;
import no.acntech.order.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    public OrderService(final OrderRepository orderRepository,
                        final OrderLineRepository orderLineRepository) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
    }

    public List<Order> findOrders(OrderQuery orderQuery) {
        UUID customerId = orderQuery.getCustomerId();
        Order.Status status = orderQuery.getStatus();
        if (customerId != null && status != null) {
            return orderRepository.findAllByCustomerIdAndStatus(customerId, status);
        } else if (customerId != null) {
            return orderRepository.findAllByCustomerId(customerId);
        } else if (status != null) {
            return orderRepository.findAllByStatus(status);
        } else {
            return orderRepository.findAll(Sort.by("id"));
        }
    }

    public Order getOrder(UUID orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Transactional
    public Order createOrder(@NotNull CreateOrder createOrder) {
        return orderRepository.save(Order.builder()
                .customerId(createOrder.getCustomerId())
                .build());
    }

    @Transactional
    public Order createOrderLine(@NotNull UUID orderId, @NotNull CreateOrderLine createOrderLine) {
        Order order = orderRepository.findByOrderId(orderId);

        orderLineRepository.save(OrderLine.builder()
                .orderId(order.getId())
                .productId(createOrderLine.getProductId())
                .quantity(createOrderLine.getQuantity())
                .build());

        order.preUpdate();
        return order;
    }
}

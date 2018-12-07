package no.acntech.order.service;

import no.acntech.order.model.*;
import no.acntech.order.producer.OrderEventProducer;
import no.acntech.order.repository.OrderLineRepository;
import no.acntech.order.repository.OrderRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(final ConversionService conversionService,
                        final OrderRepository orderRepository,
                        final OrderLineRepository orderLineRepository,
                        final OrderEventProducer orderEventProducer) {
        this.conversionService = conversionService;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.orderEventProducer = orderEventProducer;
    }

    public List<Order> findOrders(OrderQuery orderQuery) {
        UUID customerId = orderQuery.getCustomerId();
        OrderStatus status = orderQuery.getStatus();
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
        Order order = conversionService.convert(createOrder, Order.class);
        Order createdOrder = orderRepository.save(order);
        orderEventProducer.orderCreated(createdOrder.getOrderId());
        return createdOrder;
    }

    @Transactional
    public Order createOrderLine(@NotNull UUID orderId, @NotNull CreateOrderLine createOrderLine) {
        Order order = orderRepository.findByOrderId(orderId);
        createOrderLine.setOrderId(order.getId());
        OrderLine orderLine = conversionService.convert(createOrderLine, OrderLine.class);
        orderLineRepository.save(orderLine);
        order.preUpdate();
        orderEventProducer.orderUpdated(orderId, createOrderLine.getProductId(), createOrderLine.getQuantity());
        return order;
    }
}

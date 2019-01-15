package no.acntech.order.service;

import no.acntech.order.exception.OrderNotFoundException;
import no.acntech.order.model.*;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public OrderService(final OrderRepository orderRepository,
                        final ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @SuppressWarnings("Duplicates")
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
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Transactional
    public Order createOrder(@NotNull CreateOrder createOrder) {
        Order order = Order.builder()
                .customerId(createOrder.getCustomerId())
                .build();
        Order createdOrder = orderRepository.save(order);
        LOGGER.debug("Created order with order-id {}", createdOrder.getOrderId());
        return createdOrder;
    }

    @Transactional
    public Order updateOrder(@NotNull UUID orderId, @NotNull UpdateOrder updateOrder) {
        Order order = getOrder(orderId);
        order.setStatus(updateOrder.getStatus());

        Order updatedOrder = orderRepository.save(order);

        if (order.getStatus() == OrderStatus.COMPLETED) {
            LOGGER.debug("Completed order with order-id {}", updatedOrder.getOrderId());
        } else if (order.getStatus() == OrderStatus.CANCELED) {
            LOGGER.debug("Canceled order with order-id {}", updatedOrder.getOrderId());
        } else {
            LOGGER.debug("Rejected order with order-id {}", updatedOrder.getOrderId());
        }
        return updatedOrder;
    }

    @SuppressWarnings("Duplicates")
    @Transactional
    public Order createItem(@NotNull UUID orderId, @NotNull CreateItem createItem) {
        UUID productId = createItem.getProductId();
        Long quantity = createItem.getQuantity();

        Order order = getOrder(orderId);

        Item item = Item.builder()
                .orderId(order.getId())
                .productId(productId)
                .quantity(quantity)
                .build();

        itemRepository.save(item);
        order.preUpdate();

        LOGGER.debug("Updated order with order-id {} for product-id {}", orderId, productId);
        return order;
    }
}

package no.acntech.order.service;

import no.acntech.order.exception.OrderNotFoundException;
import no.acntech.order.model.*;
import no.acntech.order.producer.OrderEventProducer;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(final OrderRepository orderRepository,
                        final ItemRepository itemRepository,
                        final OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
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
        orderEventProducer.orderCreated(createdOrder.getOrderId());
        return createdOrder;
    }

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
        orderEventProducer.orderUpdated(orderId, productId, quantity);
        return order;
    }

    @Transactional
    public void updateItem(@NotNull UpdateItem updateItem) {
        UUID orderId = updateItem.getOrderId();
        UUID productId = updateItem.getProductId();
        ItemStatus status = updateItem.getStatus();

        Order order = getOrder(orderId);
        Optional<Item> itemOptional = itemRepository.findByOrderIdAndProductId(order.getId(), productId);

        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            item.setStatus(status);

            itemRepository.save(item);
            order.preUpdate();

            LOGGER.debug("Updated order with order-id {} for product-id {}", orderId, productId);
        } else {
            LOGGER.debug("Order item for order-id {} for product-id {} not found", orderId, productId);
        }
    }
}

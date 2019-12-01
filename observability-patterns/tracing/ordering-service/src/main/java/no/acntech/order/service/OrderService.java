package no.acntech.order.service;

import brave.ScopedSpan;
import brave.Tracer;
import no.acntech.order.exception.ItemAlreadyExistsException;
import no.acntech.order.exception.ItemNotFoundException;
import no.acntech.order.exception.OrderNotFoundException;
import no.acntech.order.exception.ReservationIdNotReceivedException;
import no.acntech.order.model.*;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");

    private final Tracer tracer;
    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ReservationRestConsumer reservationRestConsumer;

    public OrderService(final Tracer tracer,
                        final ConversionService conversionService,
                        final OrderRepository orderRepository,
                        final ItemRepository itemRepository,
                        final ReservationRestConsumer reservationRestConsumer) {
        this.tracer = tracer;
        this.conversionService = conversionService;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.reservationRestConsumer = reservationRestConsumer;
    }

    public List<OrderDto> findOrders(@NotNull final OrderQuery orderQuery) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#findOrders");
        try {
            UUID customerId = orderQuery.getCustomerId();
            OrderStatus status = orderQuery.getStatus();

            if (customerId != null && status != null) {
                return orderRepository.findAllByCustomerIdAndStatus(customerId, status)
                        .stream()
                        .map(this::convert)
                        .collect(Collectors.toList());
            } else if (customerId != null) {
                return orderRepository.findAllByCustomerId(customerId)
                        .stream()
                        .map(this::convert)
                        .collect(Collectors.toList());
            } else if (status != null) {
                return orderRepository.findAllByStatus(status)
                        .stream()
                        .map(this::convert)
                        .collect(Collectors.toList());
            } else {
                return orderRepository.findAll(SORT_BY_ID)
                        .stream()
                        .map(this::convert)
                        .collect(Collectors.toList());
            }
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#getOrder");
        try {
            Order order = getOrderByOrderId(orderId);
            return convert(order);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrder) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#createOrder");
        try {
            Order order = conversionService.convert(createOrder, Order.class);
            Assert.notNull(order, "Failed to convert order");

            Order createdOrder = orderRepository.save(order);

            LOGGER.debug("Created order with order-id {}", createdOrder.getOrderId());
            return convert(createdOrder);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public void updateOrder(@NotNull final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#updateOrder");
        try {
            Order order = getOrderByOrderId(orderId);

            order.getItems().stream()
                    .map(Item::getReservationId)
                    .forEach(this::confirmReservation);

            LOGGER.debug("Updated order with order-id {}", orderId);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public void deleteOrder(@NotNull final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#deleteOrder");
        try {
            Order order = getOrderByOrderId(orderId);

            order.cancelOrder();

            order.getItems().stream()
                    .map(Item::getReservationId)
                    .forEach(reservationId -> {
                        final Optional<ReservationDto> delete = reservationRestConsumer.delete(reservationId);
                        delete.ifPresent(e -> updateItemReservation(reservationId, e.getQuantity(), ItemStatus.valueOf(e.getStatus().name())));

                    });

            LOGGER.debug("Deleting order with order-id {}", orderId);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public ItemDto getItem(@NotNull final UUID itemId) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#getItem");
        try {
            Item item = getItemByItemId(itemId);
            Order order = getOrderById(item.getOrderId());
            OrderDto orderDto = convert(order);
            return orderDto.getItems().stream()
                    .filter(itemDto -> itemDto.getItemId().equals(itemId))
                    .findFirst()
                    .orElseThrow(() -> new ItemNotFoundException(itemId));
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public OrderDto createItem(@NotNull final UUID orderId, @Valid final CreateItemDto createItem) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#createItem");
        try {
            UUID productId = createItem.getProductId();
            Long quantity = createItem.getQuantity();

            Order order = getOrderByOrderId(orderId);
            Optional<Item> exitingItem = itemRepository.findByOrderIdAndProductId(order.getId(), productId);

            if (!exitingItem.isPresent()) {
                CreateReservationDto createReservation = CreateReservationDto.builder()
                        .orderId(orderId)
                        .productId(productId)
                        .quantity(quantity)
                        .build();

                Optional<ReservationDto> pendingReservationOptional = reservationRestConsumer.create(createReservation);

                if (pendingReservationOptional.isPresent()) {
                    ReservationDto pendingReservation = pendingReservationOptional.get();

                    Item item = Item.builder()
                            .orderId(order.getId())
                            .productId(productId)
                            .reservationId(pendingReservation.getReservationId())
                            .quantity(quantity)
                            .status(ItemStatus.valueOf(pendingReservation.getStatus().name()))
                            .build();

                    itemRepository.save(item);

                    LOGGER.debug("Created order item with product-id {} for order-id {}", orderId, productId);

                    Order updatedOrder = getOrderByOrderId(orderId);
                    return convert(updatedOrder);
                } else {
                    throw new ReservationIdNotReceivedException(orderId, productId);
                }
            } else {
                throw new ItemAlreadyExistsException(orderId, productId);
            }
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public void updateItem(@NotNull final UUID itemId, @Valid final UpdateItemDto updateItem) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#updateItem");
        try {
            Item item = getItemByItemId(itemId);
            Order order = getOrderById(item.getOrderId());

            UUID orderId = order.getOrderId();
            UUID productId = item.getProductId();
            UUID reservationId = item.getReservationId();
            Long quantity = updateItem.getQuantity();

            UpdateReservationDto updateReservation = UpdateReservationDto.builder()
                    .quantity(quantity)
                    .build();
            reservationRestConsumer.update(reservationId, updateReservation);

            LOGGER.debug("Updated order item for order-id {} and product-id {}", orderId, productId);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public void deleteItem(@NotNull final UUID itemId) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#deleteItem");
        try {
            Item item = getItemByItemId(itemId);
            Order order = getOrderById(item.getOrderId());

            UUID orderId = order.getOrderId();
            UUID productId = item.getProductId();
            UUID reservationId = item.getReservationId();

            final Optional<ReservationDto> delete = reservationRestConsumer.delete(reservationId);
            if (delete.isPresent()) {
                final ReservationDto reservation = delete.get();
                updateItemReservation(reservationId, reservation.getQuantity(), ItemStatus.valueOf(reservation.getStatus().name()));
            }

            LOGGER.debug("Deleted order item with product-id {} for order-id {}", orderId, productId);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public Optional<UUID> updateItemReservation(@NotNull UUID reservationId, @NotNull Long quantity, @NotNull ItemStatus status) {
        ScopedSpan span = tracer.startScopedSpan("OrderService#updateItemReservation");
        try {
            Optional<Item> exitingItem = itemRepository.findByReservationId(reservationId);

            if (exitingItem.isPresent()) {
                Item item = exitingItem.get();

                Optional<Order> existingOrder = orderRepository.findById(item.getOrderId());

                if (existingOrder.isPresent()) {
                    Order order = existingOrder.get();
                    UUID orderId = order.getOrderId();

                    item.setStatus(status);
                    item.setQuantity(quantity);

                    LOGGER.debug("Updating order item status to {} for order-id {} and reservation-id {}", status.name(), orderId, reservationId);

                    itemRepository.save(item);

                    OrderStatus orderStatus = nextOrderStatus(order);

                    if (OrderStatus.CONFIRMED.equals(orderStatus)) {
                        LOGGER.debug("Updating order status to {} for order-id {}", orderStatus.name(), orderId);
                        order.confirmOrder();
                        orderRepository.save(order);
                    }

                    return Optional.of(orderId);
                } else {
                    LOGGER.error("Could not find order for reservation-id {}", reservationId);
                    return Optional.empty();
                }
            } else {
                LOGGER.error("Could not find order item for reservation-id {}", reservationId);
                return Optional.empty();
            }
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    private Order getOrderById(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private Order getOrderByOrderId(final UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private Item getItemByItemId(final UUID itemId) {
        return itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    private OrderStatus nextOrderStatus(final Order order) {
        if (areAllActiveItemsConfirmed(order)) {
            return OrderStatus.CONFIRMED;
        } else {
            return null;
        }
    }

    private void confirmReservation(UUID reservationId) {
        final UpdateReservationDto updateReservation = UpdateReservationDto.builder()
                .statusConfirmed()
                .build();

        LOGGER.debug("Updating reservation status to {} for reservation-id {}", updateReservation.getStatus().name(), reservationId);
        final Optional<ReservationDto> update = reservationRestConsumer.update(reservationId, updateReservation);

        if (update.isPresent()) {
            final ReservationDto reservation = update.get();
            updateItemReservation(reservationId, reservation.getQuantity(), ItemStatus.valueOf(reservation.getStatus().name()));

        }
    }

    private boolean areAllActiveItemsConfirmed(final Order order) {
        List<Item> activeItems = order.getItems().stream()
                .filter(activeItem -> !ItemStatus.CANCELED.equals(activeItem.getStatus()))
                .collect(Collectors.toList());
        boolean allActiveItemsConfirmed = activeItems.stream()
                .map(Item::getStatus)
                .allMatch(ItemStatus.CONFIRMED::equals);
        return !activeItems.isEmpty() && allActiveItemsConfirmed;
    }

    private OrderDto convert(final Order order) {
        return conversionService.convert(order, OrderDto.class);
    }
}

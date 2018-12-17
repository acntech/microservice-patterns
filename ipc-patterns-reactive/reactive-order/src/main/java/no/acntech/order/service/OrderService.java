package no.acntech.order.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.order.entity.CreateItem;
import no.acntech.order.entity.CreateOrder;
import no.acntech.order.entity.Order;
import no.acntech.order.entity.Orderline;
import no.acntech.order.integration.warehouse.WarehouseConsumer;
import no.acntech.order.repository.OrderRepository;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final WarehouseConsumer warehouseConsumer;
    //    private final ShippingRestClient shippingRestClient;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        WarehouseConsumer warehouseConsumer)
    //                        WarehouseRestClient warehouseRestClient,
    //                        ShippingRestClient shippingRestClient)
    {
        this.orderRepository = orderRepository;
        this.warehouseConsumer = warehouseConsumer;
        //        this.warehouseRestClient = warehouseRestClient;
        //        this.shippingRestClient = shippingRestClient;
    }

    /**
     * Orchestrates and calls other services synchronously.
     * If we have an error after the services has responded we have no rollback (only local transactions).
     * This also implies 100% uptime requirement on other services.
     */
    public Mono<Order> createOrder(CreateOrder createOrder) {
        final Order order = Order.builder()
                .customerId(createOrder.getCustomerId())
                .build();
        return orderRepository.save(order).delayElement(Duration.ofSeconds(2));
        //        order = orderRepository.save(order);
        //        order.setOrderstatus(Orderstatus.COMPLETED);
        //
        //        String warehouseReservationId = warehouseRestClient.reserve(order);
        //        order.setWarehouseReservationId(warehouseReservationId);
        //        LOGGER.info("Reservation in warehouse completed! orderId={} reservationId={}", order.getId(), warehouseReservationId);
        //
        //        shippingRestClient.ship(order);
        //        order.setShipped(true);
        //        LOGGER.info("Order with orderId={} shipped!", order.getId());

        //        return order;
    }

    public Mono<Order> addItem(String orderId, CreateItem item) {
        return orderRepository.findById(orderId)
                .flatMap(o -> {
                    o.addOrderline(Orderline.builder().productId(item.getProductId()).quantity(item.getQuantity()).build());
                    return orderRepository.save(o).delayElement(Duration.ofSeconds(2));
                })
                .doOnNext(warehouseConsumer::reserve);
    }

    public Flux<Order> findAllOrders() {
        return orderRepository.findAll().delayElements(Duration.ofSeconds(2));
    }

    public Mono<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    //    public Mono<Order> update(String id, Order order) {
    //        return this.orderRepository.findById(id)
    //                .map(p -> {
    //                    p.setOrderlines(order.getOrderlines());
    //                    p.setOrderstatus(order.getOrderstatus());
    //                    p.setShipped(order.isShipped());
    //                    p.setWarehouseReservationId(order.getWarehouseReservationId());
    //                    return p;
    //                })
    //                .flatMap(this.orderRepository::save);
    //    }

    public Mono<Void> delete(String id) {
        return orderRepository.deleteById(id);
    }
}

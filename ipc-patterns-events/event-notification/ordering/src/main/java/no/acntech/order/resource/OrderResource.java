package no.acntech.order.resource;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import no.acntech.order.model.CreateOrder;
import no.acntech.order.model.CreateOrderLine;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.service.OrderService;

@RequestMapping(path = "orders")
@RestController
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> get(OrderQuery orderQuery) {
        return orderService.findOrders(orderQuery);
    }

    @GetMapping(path = "{orderId}")
    public Order get(@PathVariable("orderId") UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody CreateOrder createOrder) {
        Order order = orderService.createOrder(createOrder);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping(path = "{orderId}/lines")
    public ResponseEntity postLine(@PathVariable("orderId") UUID orderId, @RequestBody CreateOrderLine createOrderLine) {
        Order order = orderService.createOrderLine(orderId, createOrderLine);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

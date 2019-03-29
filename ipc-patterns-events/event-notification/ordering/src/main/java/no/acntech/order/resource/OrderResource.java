package no.acntech.order.resource;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import no.acntech.order.model.CreateItem;
import no.acntech.order.model.CreateOrder;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.UpdateOrder;
import no.acntech.order.service.OrderService;

@SuppressWarnings("Duplicates")
@RequestMapping(path = "orders")
@RestController
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> get(final OrderQuery orderQuery) {
        List<Order> orders = orderService.findOrders(orderQuery);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<Order> get(@PathVariable("orderId") final UUID orderId) {
        final Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateOrder createOrder) {
        final Order order = orderService.createOrder(createOrder);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "{orderId}")
    public ResponseEntity put(@PathVariable("orderId") final UUID orderId,
                              @Valid @RequestBody final UpdateOrder updateOrder) {
        final Order order = orderService.updateOrder(orderId, updateOrder);
        return ResponseEntity.ok(order);
    }

    @PostMapping(path = "{orderId}/items")
    public ResponseEntity postItem(@PathVariable("orderId") final UUID orderId,
                                   @Valid @RequestBody final CreateItem createItem) {
        final Order order = orderService.createItem(orderId, createItem);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

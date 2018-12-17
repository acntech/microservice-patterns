package no.acntech.order.resource;

import no.acntech.order.model.*;
import no.acntech.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "orders")
@RestController
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> get(final OrderQuery orderQuery) {
        return orderService.findOrders(orderQuery);
    }

    @GetMapping(path = "{orderId}")
    public Order get(@PathVariable("orderId") final UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateOrder createOrder) {
        Order order = orderService.createOrder(createOrder);
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
        Order order = orderService.updateOrder(orderId, updateOrder);
        return ResponseEntity.ok(order);
    }

    @SuppressWarnings("Duplicates")
    @PostMapping(path = "{orderId}/items")
    public ResponseEntity postItem(@PathVariable("orderId") final UUID orderId,
                                   @Valid @RequestBody final CreateItem createItem) {
        Order order = orderService.createItem(orderId, createItem);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

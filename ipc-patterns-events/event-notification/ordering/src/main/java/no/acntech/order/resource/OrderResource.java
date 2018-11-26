package no.acntech.order.resource;

import no.acntech.order.model.CreateOrder;
import no.acntech.order.model.CreateOrderLine;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderQuery;
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

    @PostMapping(path = "{orderId}/lines")
    public ResponseEntity postLine(@PathVariable("orderId") final UUID orderId,
                                   @Valid @RequestBody final CreateOrderLine createOrderLine) {
        Order order = orderService.createOrderLine(orderId, createOrderLine);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

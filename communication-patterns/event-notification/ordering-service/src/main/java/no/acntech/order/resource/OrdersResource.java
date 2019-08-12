package no.acntech.order.resource;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import no.acntech.order.model.CreateItemDto;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.service.OrderService;

@SuppressWarnings("Duplicates")
@RequestMapping(path = "orders")
@RestController
public class OrdersResource {

    private final OrderService orderService;

    public OrdersResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> find(final OrderQuery orderQuery) {
        List<OrderDto> orders = orderService.findOrders(orderQuery);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<OrderDto> get(@PathVariable("id") final UUID orderId) {
        OrderDto order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateOrderDto createOrder) {
        OrderDto order = orderService.createOrder(createOrder);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .pathSegment(order.getOrderId().toString())
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "{id}")
    public ResponseEntity put(@PathVariable("id") final UUID orderId) {
        orderService.updateOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity delete(@PathVariable("id") final UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{id}/items")
    public ResponseEntity postItem(@PathVariable("id") final UUID orderId,
                                   @Valid @RequestBody final CreateItemDto createItem) {
        OrderDto order = orderService.createItem(orderId, createItem);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .pathSegment(order.getOrderId().toString())
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

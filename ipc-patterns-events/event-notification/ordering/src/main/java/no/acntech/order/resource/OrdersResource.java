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
        final List<OrderDto> orders = orderService.findOrders(orderQuery);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<OrderDto> get(@PathVariable("orderId") final UUID orderId) {
        final OrderDto order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateOrderDto createOrder) {
        final OrderDto order = orderService.createOrder(createOrder);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "{orderId}")
    public ResponseEntity<OrderDto> put(@PathVariable("orderId") final UUID orderId) {
        final OrderDto order = orderService.updateOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping(path = "{orderId}")
    public ResponseEntity<OrderDto> delete(@PathVariable("orderId") final UUID orderId) {
        final OrderDto order = orderService.deleteOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping(path = "{orderId}/items")
    public ResponseEntity postItem(@PathVariable("orderId") final UUID orderId,
                                   @Valid @RequestBody final CreateItemDto createItem) {
        final OrderDto order = orderService.createItem(orderId, createItem);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "{orderId}/items")
    public ResponseEntity<OrderDto> putItem(@PathVariable("orderId") final UUID orderId,
                                            @Valid @RequestBody final UpdateItemDto updateItem) {
        final OrderDto order = orderService.updateItem(orderId, updateItem);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping(path = "{orderId}/items")
    public ResponseEntity<OrderDto> deleteItem(@PathVariable("orderId") final UUID orderId,
                                               @Valid @RequestBody final DeleteItemDto deleteItemDto) {
        final OrderDto order = orderService.deleteItem(orderId, deleteItemDto);
        return ResponseEntity.ok(order);
    }
}

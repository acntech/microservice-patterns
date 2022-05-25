package no.acntech.order.resource;

import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.service.OrderOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@RequestMapping(path = "/api/orders")
@RestController
public class OrdersResource {

    private final OrderOrchestrationService orderOrchestrationService;

    public OrdersResource(final OrderOrchestrationService orderOrchestrationService) {
        this.orderOrchestrationService = orderOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> find(final OrderQuery orderQuery) {
        final var orderDtos = orderOrchestrationService.findOrders(orderQuery);
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<OrderDto> get(@PathVariable("id") final UUID orderId) {
        final var orderDto = orderOrchestrationService.getOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping
    public ResponseEntity<OrderDto> post(@RequestBody final CreateOrderDto createOrder) {
        final var orderDto = orderOrchestrationService.createOrder(createOrder);
        final var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .pathSegment(orderDto.getOrderId().toString())
                .build()
                .toUri();
        return ResponseEntity
                .created(location)
                .body(orderDto);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Void> put(@PathVariable("id") final UUID orderId) {
        orderOrchestrationService.updateOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final UUID orderId) {
        orderOrchestrationService.deleteOrder(orderId);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping(path = "{id}/items")
    public ResponseEntity<OrderDto> postItem(@PathVariable("id") final UUID orderId,
                                             @RequestBody final CreateOrderItemDto createItem) {
        final var orderDto = orderOrchestrationService.createOrderItem(orderId, createItem);
        final var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .pathSegment(orderDto.getOrderId().toString())
                .build()
                .toUri();
        return ResponseEntity
                .created(location)
                .body(orderDto);
    }
}

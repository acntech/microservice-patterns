package no.acntech.order.resource;

import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderItemDto;
import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.order.service.OrderOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping(path = "/api/items")
@RestController
public class OrderItemsResource {

    private final OrderOrchestrationService orderOrchestrationService;

    public OrderItemsResource(final OrderOrchestrationService orderOrchestrationService) {
        this.orderOrchestrationService = orderOrchestrationService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<OrderItemDto> get(@PathVariable("id") final UUID itemId) {
        final var orderItemDto = orderOrchestrationService.getOrderItem(itemId);
        return ResponseEntity.ok(orderItemDto);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<OrderDto> put(@PathVariable("id") final UUID itemId,
                                        @RequestBody final UpdateOrderItemDto updateOrderItemDto) {
        final var orderDto = orderOrchestrationService.updateOrderItem(itemId, updateOrderItemDto);
        return ResponseEntity.ok(orderDto);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<OrderDto> delete(@PathVariable("id") final UUID itemId) {
        final var orderDto = orderOrchestrationService.deleteOrderItem(itemId);
        return ResponseEntity.ok(orderDto);
    }
}

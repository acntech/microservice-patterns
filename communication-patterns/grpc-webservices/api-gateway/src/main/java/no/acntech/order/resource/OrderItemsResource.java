package no.acntech.order.resource;

import no.acntech.order.consumer.OrderItemConsumer;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderItemDto;
import no.acntech.order.model.UpdateOrderItemDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SuppressWarnings("Duplicates")
@RequestMapping(path = "/api/items")
@RestController
public class OrderItemsResource {

    private final OrderItemConsumer orderItemConsumer;

    public OrderItemsResource(final OrderItemConsumer orderItemConsumer) {
        this.orderItemConsumer = orderItemConsumer;
    }

    @GetMapping(path = "{id}")
    public Mono<OrderItemDto> get(@PathVariable("id") final UUID orderId) {
        return orderItemConsumer.getOrderItem(orderId);
    }

    @PutMapping(path = "{id}")
    public Mono<OrderDto> put(@PathVariable("id") final UUID itemId,
                              @RequestBody final UpdateOrderItemDto updateOrderItemDto) {
        return orderItemConsumer.updateOrderItem(itemId, updateOrderItemDto);
    }

    @DeleteMapping(path = "{id}")
    public Mono<OrderDto> delete(@PathVariable("id") final UUID orderId) {
        return orderItemConsumer.deleteOrderItem(orderId);
    }
}

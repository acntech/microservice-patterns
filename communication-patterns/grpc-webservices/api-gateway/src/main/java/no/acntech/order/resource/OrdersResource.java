package no.acntech.order.resource;

import no.acntech.order.consumer.OrderConsumer;
import no.acntech.order.consumer.OrderItemConsumer;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SuppressWarnings("Duplicates")
@RequestMapping(path = "/api/orders")
@RestController
public class OrdersResource {

    private final OrderConsumer orderConsumer;
    private final OrderItemConsumer orderItemConsumer;

    public OrdersResource(final OrderConsumer orderConsumer,
                          final OrderItemConsumer orderItemConsumer) {
        this.orderConsumer = orderConsumer;
        this.orderItemConsumer = orderItemConsumer;
    }

    @GetMapping(path = "{id}")
    public Mono<OrderDto> get(@PathVariable("id") final UUID orderId) {
        return orderConsumer.getOrder(orderId);
    }

    @GetMapping
    public Flux<OrderDto> find(final OrderQuery orderQuery) {
        return orderConsumer.findOrders(orderQuery);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<OrderDto>> post(@RequestBody final CreateOrderDto createOrder,
                                               final UriComponentsBuilder uriComponentsBuilder) {
        return orderConsumer.createOrder(createOrder)
                .map(body -> {
                    final var location = uriComponentsBuilder
                            .pathSegment(body.getOrderId().toString())
                            .build()
                            .toUri();
                    return ResponseEntity
                            .created(location)
                            .body(body);
                });
    }

    @PutMapping(path = "{id}")
    public Mono<OrderDto> put(@PathVariable("id") final UUID orderId) {
        return orderConsumer.updateOrder(orderId);
    }

    @DeleteMapping(path = "{id}")
    public Mono<OrderDto> delete(@PathVariable("id") final UUID orderId) {
        return orderConsumer.deleteOrder(orderId);
    }

    @PostMapping(path = "{id}/items")
    public Mono<OrderDto> postItem(@PathVariable("id") final UUID orderId,
                                   @RequestBody final CreateOrderItemDto createItem) {
        return orderItemConsumer.createOrderItem(orderId, createItem);
    }
}

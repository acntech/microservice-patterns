package no.acntech.order.resource;

import brave.ScopedSpan;
import brave.Tracer;
import no.acntech.order.model.CreateItemDto;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
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

    private final Tracer tracer;
    private final OrderService orderService;

    public OrdersResource(final Tracer tracer,
                          final OrderService orderService) {
        this.tracer = tracer;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> find(final OrderQuery orderQuery) {
        ScopedSpan span = tracer.startScopedSpan("OrdersResource#find");
        try {
            List<OrderDto> orders = orderService.findOrders(orderQuery);
            return ResponseEntity.ok(orders);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<OrderDto> get(@PathVariable("orderId") final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("OrdersResource#get");
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(order);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateOrderDto createOrder) {
        ScopedSpan span = tracer.startScopedSpan("OrdersResource#post");
        try {
            OrderDto order = orderService.createOrder(createOrder);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .pathSegment(order.getOrderId().toString())
                    .build()
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @PutMapping(path = "{orderId}")
    public ResponseEntity put(@PathVariable("orderId") final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("OrdersResource#put");
        try {
            orderService.updateOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @DeleteMapping(path = "{orderId}")
    public ResponseEntity delete(@PathVariable("orderId") final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("OrdersResource#delete");
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @PostMapping(path = "{orderId}/items")
    public ResponseEntity postItem(@PathVariable("orderId") final UUID orderId,
                                   @Valid @RequestBody final CreateItemDto createItem) {
        ScopedSpan span = tracer.startScopedSpan("OrdersResource#postItem");
        try {
            OrderDto order = orderService.createItem(orderId, createItem);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .pathSegment(order.getOrderId().toString())
                    .build()
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }
}

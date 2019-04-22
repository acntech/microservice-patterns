package no.acntech.order.resource;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
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
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.UpdateItemDto;
import no.acntech.order.model.UpdateOrderDto;
import no.acntech.order.service.OrderService;

@SuppressWarnings("Duplicates")
@RequestMapping(path = "orders")
@RestController
public class OrderResource {

    private final ConversionService conversionService;
    private final OrderService orderService;

    public OrderResource(final ConversionService conversionService,
                         final OrderService orderService) {
        this.conversionService = conversionService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> get(final OrderQuery orderQuery) {
        final List<Order> orders = orderService.findOrders(orderQuery);
        final List<OrderDto> orderDtos = orders.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<OrderDto> get(@PathVariable("orderId") final UUID orderId) {
        final Order order = orderService.getOrder(orderId);
        final OrderDto orderDto = convert(order);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateOrderDto createOrder) {
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
                              @Valid @RequestBody final UpdateOrderDto updateOrder) {
        final Order order = orderService.updateOrder(orderId, updateOrder);
        return ResponseEntity.ok(order);
    }

    @PostMapping(path = "{orderId}/items")
    public ResponseEntity postItem(@PathVariable("orderId") final UUID orderId,
                                   @Valid @RequestBody final CreateItemDto createItem) {
        final Order order = orderService.createItem(orderId, createItem);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getOrderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "{orderId}/items")
    public ResponseEntity putItem(@PathVariable("orderId") final UUID orderId,
                                  @Valid @RequestBody final UpdateItemDto updateItem) {
        final Order order = orderService.updateItem(orderId, updateItem);
        return ResponseEntity.ok(order);
    }

    private OrderDto convert(final Order order) {
        return conversionService.convert(order, OrderDto.class);
    }
}

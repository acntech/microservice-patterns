package no.acntech.order.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
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
    public ResponseEntity<List<OrderDto>> find(@NotNull OrderQuery orderQuery,
                                               @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        List<OrderDto> orders = orderService.findOrders(orderQuery, authorizedClient);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<OrderDto> get(@PathVariable("orderId") UUID orderId,
                                        @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        OrderDto order = orderService.getOrder(orderId, authorizedClient);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody CreateOrderDto createOrder,
                               @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        URI orderLocation = orderService.createOrder(createOrder, authorizedClient);
        String[] parts = orderLocation.getPath().split("/");
        String orderId = parts[parts.length - 1];

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "{orderId}")
    public ResponseEntity put(@PathVariable("orderId") UUID orderId,
                              @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        orderService.updateOrder(orderId, authorizedClient);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{orderId}")
    public ResponseEntity delete(@PathVariable("orderId") UUID orderId,
                                 @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        orderService.deleteOrder(orderId, authorizedClient);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{orderId}/items")
    public ResponseEntity postItem(@PathVariable("orderId") UUID orderId,
                                   @Valid @RequestBody CreateItemDto createItem,
                                   @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        URI itemLocation = orderService.createItem(orderId, createItem, authorizedClient);
        String[] parts = itemLocation.getPath().split("/");
        String itemId = parts[parts.length - 1];

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(itemId)
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

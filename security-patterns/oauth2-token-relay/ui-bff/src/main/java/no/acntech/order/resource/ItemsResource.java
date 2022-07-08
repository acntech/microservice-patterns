package no.acntech.order.resource;

import no.acntech.order.model.ItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.UpdateItemDto;
import no.acntech.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(path = "items")
@RestController
public class ItemsResource {

    private final OrderService orderService;

    public ItemsResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "{itemId}")
    public ResponseEntity<ItemDto> get(@PathVariable("itemId") UUID itemId,
                                       @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        ItemDto item = orderService.getItem(itemId, authorizedClient);
        return ResponseEntity.ok(item);
    }

    @PutMapping(path = "{itemId}")
    public ResponseEntity<OrderDto> put(@PathVariable("itemId") UUID itemId,
                                        @Valid @RequestBody UpdateItemDto updateItem,
                                        @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        orderService.updateItem(itemId, updateItem, authorizedClient);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{itemId}")
    public ResponseEntity delete(@PathVariable("itemId") UUID itemId,
                                 @RegisteredOAuth2AuthorizedClient("microservice") OAuth2AuthorizedClient authorizedClient) {
        orderService.deleteItem(itemId, authorizedClient);
        return ResponseEntity.ok().build();
    }
}

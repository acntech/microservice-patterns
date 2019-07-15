package no.acntech.order.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import no.acntech.config.ServiceProperties;
import no.acntech.order.model.CreateItemDto;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.ItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.UpdateItemDto;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class OrderService {

    private final ServiceProperties properties;
    private final WebClient webClient;

    public OrderService(final ServiceProperties properties,
                        final WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    public List<OrderDto> findOrders(@NotNull OrderQuery orderQuery, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getOrders().getUrl();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        if (orderQuery.getCustomerId() != null) {
            uriComponentsBuilder.queryParam("customerId", orderQuery.getCustomerId());
        }
        if (orderQuery.getStatus() != null) {
            uriComponentsBuilder.queryParam("status", orderQuery.getStatus());
        }
        URI uri = uriComponentsBuilder.build()
                .toUri();
        return webClient.get()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToFlux(OrderDto.class)
                .collectList()
                .block();
    }

    public OrderDto getOrder(@NotNull UUID orderId, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getOrders().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();
        return webClient.get()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(OrderDto.class)
                .block();
    }

    public URI createOrder(@Valid CreateOrderDto createOrder, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getOrders().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        ClientResponse response = webClient.post()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .syncBody(createOrder)
                .exchange()
                .block();

        ClientResponse.Headers headers = response.headers();

        HttpHeaders httpHeaders = headers.asHttpHeaders();
        return httpHeaders.getLocation();
    }

    public void updateOrder(@NotNull UUID orderId, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getOrders().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();
        webClient.put()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void deleteOrder(@NotNull UUID orderId, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getOrders().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();
        webClient.delete()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public ItemDto getItem(@NotNull final UUID itemId, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getItems().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{itemId}")
                .buildAndExpand(itemId)
                .toUri();
        return webClient.get()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();
    }

    public URI createItem(@NotNull UUID orderId, @Valid CreateItemDto createItem, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getOrders().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{orderId}/items")
                .buildAndExpand(orderId)
                .toUri();

        ClientResponse response = webClient.post()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .syncBody(createItem)
                .exchange()
                .block();

        ClientResponse.Headers headers = response.headers();

        HttpHeaders httpHeaders = headers.asHttpHeaders();
        return httpHeaders.getLocation();
    }

    public void updateItem(@NotNull UUID itemId, @Valid UpdateItemDto updateItem, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getItems().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{itemId}")
                .buildAndExpand(itemId)
                .toUri();
        webClient.put()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .syncBody(updateItem)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void deleteItem(@NotNull UUID itemId, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getItems().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{itemId}")
                .buildAndExpand(itemId)
                .toUri();
        webClient.delete()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}

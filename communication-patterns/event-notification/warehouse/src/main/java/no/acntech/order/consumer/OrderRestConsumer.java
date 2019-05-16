package no.acntech.order.consumer;

import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import no.acntech.order.model.OrderDto;

@SuppressWarnings("Duplicates")
@Component
public class OrderRestConsumer {

    private static final ParameterizedTypeReference<List<OrderDto>> ORDER_LIST = new ParameterizedTypeReference<List<OrderDto>>() {
    };
    private final RestTemplate restTemplate;
    private final String url;
    private final String idUrl;

    public OrderRestConsumer(final RestTemplateBuilder restTemplateBuilder,
                             @Value("${acntech.service.ordering.api.orders.url}") final String url,
                             @Value("${acntech.service.ordering.api.orders.id.url}") final String idUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
        this.idUrl = idUrl;
    }

    public List<OrderDto> find() {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        final ResponseEntity<List<OrderDto>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ORDER_LIST);

        return entity.getBody();
    }

    public OrderDto get(@NotNull final UUID orderId) {
        Assert.notNull(orderId, "Order ID is null");

        final URI uri = UriComponentsBuilder.fromUriString(idUrl)
                .buildAndExpand(orderId.toString())
                .toUri();

        final ResponseEntity<OrderDto> entity = restTemplate.getForEntity(uri, OrderDto.class);
        return entity.getBody();
    }
}

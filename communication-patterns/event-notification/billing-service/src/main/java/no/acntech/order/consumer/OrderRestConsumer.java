package no.acntech.order.consumer;

import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import no.acntech.order.model.OrderDto;

@SuppressWarnings("Duplicates")
@Component
public class OrderRestConsumer {

    private final RestTemplate restTemplate;
    private final String url;

    public OrderRestConsumer(final RestTemplateBuilder restTemplateBuilder,
                             @Value("${acntech.service.ordering.api.orders.url}") final String url) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
    }

    public Optional<OrderDto> get(@NotNull final UUID orderId) {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(orderId.toString())
                .build()
                .toUri();

        final ResponseEntity<OrderDto> entity = restTemplate.getForEntity(uri, OrderDto.class);
        return Optional.of(entity)
                .map(ResponseEntity::getBody);
    }
}

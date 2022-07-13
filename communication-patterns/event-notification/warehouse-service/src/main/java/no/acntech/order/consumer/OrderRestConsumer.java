package no.acntech.order.consumer;

import no.acntech.order.model.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@Component
public class OrderRestConsumer {

    private final WebClient webClient;
    private final String url;

    public OrderRestConsumer(final WebClient webClient,
                             @Value("${acntech.service.ordering.url}/api/orders") final String url) {
        this.webClient = webClient;
        this.url = url;
    }

    public Optional<OrderDto> get(@NotNull final UUID orderId) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(orderId.toString())
                .build()
                .toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(OrderDto.class)
                .blockOptional();
    }
}

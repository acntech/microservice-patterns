package no.acntech.shipment.consumer;

import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.model.ShipmentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Component
public class ShipmentRestConsumer {

    private final WebClient webClient;
    private final String url;

    public ShipmentRestConsumer(final WebClient webClient,
                                @Value("${app.service.shipping.url}/api/shipments") final String url) {
        this.webClient = webClient;
        this.url = url;
    }

    public ShipmentDto create(@NotNull @Valid final CreateShipmentDto createShipmentDto) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        return webClient.post()
                .uri(uri)
                .bodyValue(createShipmentDto)
                .retrieve()
                .bodyToMono(ShipmentDto.class)
                .block();
    }
}

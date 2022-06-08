package no.acntech.invoice.consumer;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.reservation.model.ReservationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class InvoiceRestConsumer {

    private final WebClient webClient;
    private final String url;

    public InvoiceRestConsumer(final WebClient webClient,
                               @Value("${acntech.service.billing.url}/api/invoices") final String url) {
        this.webClient = webClient;
        this.url = url;
    }

    public ReservationDto create(@NotNull @Valid final CreateInvoiceDto createInvoiceDto) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        return webClient.post()
                .uri(uri)
                .bodyValue(createInvoiceDto)
                .retrieve()
                .bodyToMono(ReservationDto.class)
                .block();
    }
}

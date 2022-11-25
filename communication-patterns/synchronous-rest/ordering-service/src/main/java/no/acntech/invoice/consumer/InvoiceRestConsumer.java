package no.acntech.invoice.consumer;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.model.InvoiceDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Component
public class InvoiceRestConsumer {

    private final WebClient webClient;
    private final String url;

    public InvoiceRestConsumer(final WebClient webClient,
                               @Value("${acntech.service.billing.url}/api/invoices") final String url) {
        this.webClient = webClient;
        this.url = url;
    }

    public InvoiceDto create(@NotNull @Valid final CreateInvoiceDto createInvoiceDto) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        return webClient.post()
                .uri(uri)
                .bodyValue(createInvoiceDto)
                .retrieve()
                .bodyToMono(InvoiceDto.class)
                .block();
    }
}

package no.acntech.invoice.resource;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.model.InvoiceDto;
import no.acntech.invoice.model.InvoiceQuery;
import no.acntech.invoice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/invoices")
@RestController
public class InvoiceResource {

    private final InvoiceService invoiceService;

    public InvoiceResource(final InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<InvoiceDto> get(final InvoiceQuery invoiceQuery) {
        return invoiceService.findInvoices(invoiceQuery);
    }

    @GetMapping(path = "{invoiceId}")
    public ResponseEntity<InvoiceDto> get(@PathVariable("invoiceId") final UUID invoiceId) {
        final var invoiceDto = invoiceService.getInvoice(invoiceId);
        return ResponseEntity.ok(invoiceDto);
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> create(final CreateInvoiceDto createInvoiceDto) {
        final var invoiceDto = invoiceService.createInvoice(createInvoiceDto);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{invoiceId}")
                .buildAndExpand(invoiceDto.getInvoiceId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(invoiceDto);
    }
}

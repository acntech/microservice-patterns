package no.acntech.invoice.resource;

import no.acntech.invoice.model.CreateInvoice;
import no.acntech.invoice.model.Invoice;
import no.acntech.invoice.model.InvoiceQuery;
import no.acntech.invoice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "invoices")
@RestController
public class InvoiceResource {

    private final InvoiceService invoiceService;

    public InvoiceResource(final InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> get(final InvoiceQuery invoiceQuery) {
        return invoiceService.findInvoices(invoiceQuery);
    }

    @GetMapping(path = "{invoiceId}")
    public ResponseEntity<Invoice> get(@Valid @NotNull @PathVariable("invoiceId") final UUID invoiceId) {
        return ResponseEntity.ok(invoiceService.getInvoice(invoiceId));
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateInvoice createInvoice) {
        Invoice invoice = invoiceService.createInvoice(createInvoice);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{invoiceId}")
                .buildAndExpand(invoice.getInvoiceId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

package no.acntech.invoice.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.invoice.model.InvoiceDto;
import no.acntech.invoice.model.InvoiceQuery;
import no.acntech.invoice.service.InvoiceService;

@RequestMapping(path = "invoices")
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
    public ResponseEntity<InvoiceDto> get(@Valid @NotNull @PathVariable("invoiceId") final UUID invoiceId) {
        return ResponseEntity.ok(invoiceService.getInvoice(invoiceId));
    }
}

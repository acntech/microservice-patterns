package no.acntech.invoice.service;

import no.acntech.invoice.exception.InvoiceNotFoundException;
import no.acntech.invoice.model.CreateInvoice;
import no.acntech.invoice.model.Invoice;
import no.acntech.invoice.model.InvoiceQuery;
import no.acntech.invoice.repository.InvoiceRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    private final ConversionService conversionService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(final ConversionService conversionService,
                          final InvoiceRepository invoiceRepository) {
        this.conversionService = conversionService;
        this.invoiceRepository = invoiceRepository;
    }

    @SuppressWarnings("Duplicates")
    public List<Invoice> findInvoices(final InvoiceQuery invoiceQuery) {
        UUID orderId = invoiceQuery.getOrderId();
        Invoice.Status status = invoiceQuery.getStatus();
        if (orderId != null && status != null) {
            return invoiceRepository.findAllByOrderIdAndStatus(orderId, status);
        } else if (orderId != null) {
            return invoiceRepository.findAllByOrderId(orderId);
        } else if (status != null) {
            return invoiceRepository.findAllByStatus(status);
        } else {
            return invoiceRepository.findAll(Sort.by("id"));
        }
    }

    public Invoice getInvoice(final UUID invoiceId) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

    public Invoice createInvoice(final CreateInvoice createInvoice) {
        Invoice invoice = conversionService.convert(createInvoice, Invoice.class);
        return invoiceRepository.save(invoice);
    }
}

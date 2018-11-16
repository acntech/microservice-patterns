package no.acntech.invoice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import no.acntech.invoice.model.Invoice;
import no.acntech.invoice.repository.InvoiceRepository;

@Service
public class InvoiceService {


    private final InvoiceRepository invoiceRepository;

    public InvoiceService(final InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findOrders() {
        return invoiceRepository.findAll();
    }
}

package no.acntech.invoice.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import no.acntech.invoice.exception.InvoiceNotFoundException;
import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.model.Invoice;
import no.acntech.invoice.model.InvoiceDto;
import no.acntech.invoice.model.InvoiceQuery;
import no.acntech.invoice.model.InvoiceStatus;
import no.acntech.invoice.repository.InvoiceRepository;

@Service
public class InvoiceService {

    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(final ConversionService conversionService,
                          final InvoiceRepository invoiceRepository) {
        this.conversionService = conversionService;
        this.invoiceRepository = invoiceRepository;
    }

    @SuppressWarnings("Duplicates")
    public List<InvoiceDto> findInvoices(final InvoiceQuery invoiceQuery) {
        UUID orderId = invoiceQuery.getOrderId();
        InvoiceStatus status = invoiceQuery.getStatus();
        if (orderId != null && status != null) {
            return invoiceRepository.findAllByOrderIdAndStatus(orderId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (orderId != null) {
            return invoiceRepository.findAllByOrderId(orderId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return invoiceRepository.findAllByStatus(status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return invoiceRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public InvoiceDto getInvoice(final UUID invoiceId) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .map(this::convert)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

    public InvoiceDto createInvoice(final CreateInvoiceDto createInvoice) {
        Invoice invoice = conversionService.convert(createInvoice, Invoice.class);
        Assert.notNull(invoice, "Failed to convert invoice");

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convert(savedInvoice);
    }

    private InvoiceDto convert(final Invoice invoice) {
        return conversionService.convert(invoice, InvoiceDto.class);
    }
}


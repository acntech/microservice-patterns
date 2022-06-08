package no.acntech.invoice.service;

import no.acntech.invoice.exception.InvoiceNotFoundException;
import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.model.InvoiceDto;
import no.acntech.invoice.model.InvoiceEntity;
import no.acntech.invoice.model.InvoiceQuery;
import no.acntech.invoice.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Service
public class InvoiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(final ConversionService conversionService,
                          final InvoiceRepository invoiceRepository) {
        this.conversionService = conversionService;
        this.invoiceRepository = invoiceRepository;
    }

    @SuppressWarnings("Duplicates")
    public List<InvoiceDto> findInvoices(@NotNull @Valid final InvoiceQuery invoiceQuery) {
        if (invoiceQuery.getOrderId() != null && invoiceQuery.getStatus() != null) {
            return invoiceRepository.findAllByOrderIdAndStatus(invoiceQuery.getOrderId(), invoiceQuery.getStatus())
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (invoiceQuery.getOrderId() != null) {
            return invoiceRepository.findAllByOrderId(invoiceQuery.getOrderId())
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (invoiceQuery.getStatus() != null) {
            return invoiceRepository.findAllByStatus(invoiceQuery.getStatus())
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

    public InvoiceDto getInvoice(@NotNull final UUID invoiceId) {
        return invoiceRepository.findByInvoiceId(invoiceId)
                .map(this::convert)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

    @Transactional
    public InvoiceDto createInvoice(@NotNull @Valid final CreateInvoiceDto createInvoiceDto) {
        LOGGER.debug("Creating invoice for CreateInvoiceDto with order-id {}", createInvoiceDto.getOrderId());
        final var invoiceEntity = conversionService.convert(createInvoiceDto, InvoiceEntity.class);
        Assert.notNull(invoiceEntity, "Failed to convert CreateInvoiceDto to InvoiceEntity");
        final var savedInvoice = invoiceRepository.save(invoiceEntity);
        return convert(savedInvoice);
    }

    private InvoiceDto convert(final InvoiceEntity invoiceEntity) {
        final var invoiceDto = conversionService.convert(invoiceEntity, InvoiceDto.class);
        Assert.notNull(invoiceDto, "Failed to convert InvoiceEntity to InvoiceDto");
        return invoiceDto;
    }
}


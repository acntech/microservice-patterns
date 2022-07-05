package no.acntech.invoice.consumer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.service.InvoiceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Component
public class InvoiceRabbitConsumer {

    private final InvoiceService invoiceService;

    public InvoiceRabbitConsumer(final InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @RabbitListener(queues = RabbitQueue.CREATE_INVOICE)
    public void consumeCreateInvoice(@NotNull @Valid final CreateInvoiceDto createInvoiceDto) {
        invoiceService.createInvoice(createInvoiceDto);
    }
}

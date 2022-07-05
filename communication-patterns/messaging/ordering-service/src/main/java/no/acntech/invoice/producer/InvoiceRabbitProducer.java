package no.acntech.invoice.producer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.invoice.model.CreateInvoiceDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class InvoiceRabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    public InvoiceRabbitProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void create(@NotNull @Valid final CreateInvoiceDto createInvoiceDto) {
        rabbitTemplate.convertAndSend(RabbitQueue.CREATE_INVOICE, createInvoiceDto);
    }
}

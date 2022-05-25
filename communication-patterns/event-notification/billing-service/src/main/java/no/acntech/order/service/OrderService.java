package no.acntech.order.service;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.service.InvoiceService;
import no.acntech.order.consumer.OrderRestConsumer;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final ConversionService conversionService;
    private final OrderRestConsumer orderRestConsumer;
    private final InvoiceService invoiceService;

    public OrderService(final ConversionService conversionService,
                        final OrderRestConsumer orderRestConsumer,
                        final InvoiceService invoiceService) {
        this.conversionService = conversionService;
        this.orderRestConsumer = orderRestConsumer;
        this.invoiceService = invoiceService;
    }

    @SuppressWarnings("Duplicates")
    public void processOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Processing OrderEvent with order-id {}", orderEvent.getOrderId());

        try {
            LOGGER.debug("Fetching OrderDto for order-id {}", orderEvent.getOrderId());
            final var orderOptional = orderRestConsumer.get(orderEvent.getOrderId());
            if (orderOptional.isPresent()) {
                final var order = orderOptional.get();
                processOrderDto(order);
            } else {
                LOGGER.error("OrderDto with order-id {} could not be found", orderEvent.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing OrderEvent with order-id " + orderEvent.getOrderId(), e);
        }
    }

    private void processOrderDto(final OrderDto orderDto) {
        LOGGER.debug("Processing OrderDto for order-id {}", orderDto.getOrderId());

        if (orderDto.getStatus() == OrderStatus.CONFIRMED) {
            final var createInvoiceDto = conversionService.convert(orderDto, CreateInvoiceDto.class);
            Assert.notNull(createInvoiceDto, "Failed to convert OrderDto to CreateInvoiceDto");
            invoiceService.createInvoice(createInvoiceDto);
        } else {
            LOGGER.debug("Ignoring OrderDto for order-id {} and status {}", orderDto.getOrderId(), orderDto.getStatus());
        }
    }
}

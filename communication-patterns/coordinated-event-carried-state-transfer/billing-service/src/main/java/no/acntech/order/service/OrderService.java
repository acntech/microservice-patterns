package no.acntech.order.service;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.service.InvoiceService;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
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
    private final InvoiceService invoiceService;

    public OrderService(final ConversionService conversionService,
                        final InvoiceService invoiceService) {
        this.conversionService = conversionService;
        this.invoiceService = invoiceService;
    }

    public void processOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Processing OrderEvent with order-id {}", orderEvent.getOrderId());

        try {
            processReservationEvent(orderEvent);
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order event", e);
        }
    }

    private void processReservationEvent(final OrderEvent orderEvent) {
        if (orderEvent.getEventType() == OrderEventType.ORDER_UPDATED && orderEvent.getOrderStatus() == OrderStatus.CLOSED) {
            var createInvoiceDto = conversionService.convert(orderEvent, CreateInvoiceDto.class);
            Assert.notNull(createInvoiceDto, "Failed to convert OrderEvent to CreateInvoiceDto");
            invoiceService.createInvoice(createInvoiceDto);
        } else {
            LOGGER.debug("Ignoring order event with type {} for order-id {}", orderEvent.getEventType(), orderEvent.getOrderId());
        }
    }
}

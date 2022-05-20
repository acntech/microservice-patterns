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
    public void receiveOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Fetching order for order-id {}", orderEvent.getOrderId());

        try {
            final var orderOptional = orderRestConsumer.get(orderEvent.getOrderId());
            if (orderOptional.isPresent()) {
                final var order = orderOptional.get();
                processOrder(order);
            } else {
                LOGGER.error("Order with order-id {} could not be found", orderEvent.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order", e);
        }
    }

    private void processOrder(final OrderDto orderDto) {
        LOGGER.debug("Processing orderDto for order-id {}", orderDto.getOrderId());

        if (orderDto.getStatus() == OrderStatus.CONFIRMED) {
            LOGGER.debug("Creating invoice for order with order-id {}", orderDto.getOrderId());
            final var createInvoiceDto = conversionService.convert(orderDto, CreateInvoiceDto.class);
            invoiceService.createInvoice(createInvoiceDto);
        } else {
            LOGGER.debug("Ignoring order for order-id {} and status {}", orderDto.getOrderId(), orderDto.getStatus());
        }
    }
}

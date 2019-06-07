package no.acntech.order.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.invoice.model.Invoice;
import no.acntech.invoice.repository.InvoiceRepository;
import no.acntech.order.consumer.OrderRestConsumer;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderStatus;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRestConsumer orderRestConsumer;
    private final InvoiceRepository invoiceRepository;

    public OrderService(final OrderRestConsumer orderRestConsumer,
                        final InvoiceRepository invoiceRepository) {
        this.orderRestConsumer = orderRestConsumer;
        this.invoiceRepository = invoiceRepository;
    }

    @SuppressWarnings("Duplicates")
    public void receiveOrderEvent(final OrderEvent orderEvent) {
        UUID orderId = orderEvent.getOrderId();

        LOGGER.debug("Fetching order for order-id {}", orderId);

        try {
            Optional<OrderDto> orderOptional = orderRestConsumer.get(orderId);

            if (orderOptional.isPresent()) {
                OrderDto order = orderOptional.get();

                processOrder(order);
            } else {
                LOGGER.error("Order with order-id {} could not be found", orderId);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order", e);
        }
    }

    private void processOrder(final OrderDto order) {
        UUID orderId = order.getOrderId();
        UUID customerId = order.getCustomerId();
        OrderStatus status = order.getStatus();

        LOGGER.debug("Processing order for order-id {}", orderId);

        if (status == OrderStatus.CONFIRMED) {
            LOGGER.debug("Creating invoice for order with order-id {}", orderId);

            Invoice invoice = Invoice.builder()
                    .customerId(customerId)
                    .orderId(orderId)
                    .build();
            invoiceRepository.save(invoice);
        } else {
            LOGGER.debug("Ignoring order for order-id {} and status {}", orderId, status);
        }
    }
}

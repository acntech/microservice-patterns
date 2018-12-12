package no.acntech.order.consumer;

import no.acntech.invoice.model.CreateInvoice;
import no.acntech.invoice.service.InvoiceService;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Component
public class OrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventConsumer.class);
    private static final List<String> KAFKA_TOPICS = Collections.singletonList("orders");

    private final KafkaConsumer<String, OrderEvent> kafkaConsumer;
    private final InvoiceService invoiceService;

    public OrderEventConsumer(@Qualifier("orderKafkaConsumer") final KafkaConsumer<String, OrderEvent> kafkaConsumer,
                              final InvoiceService invoiceService) {
        this.kafkaConsumer = kafkaConsumer;
        this.invoiceService = invoiceService;
    }

    @SuppressWarnings("Duplicates")
    @Async
    public void startConsumer() {
        try {
            kafkaConsumer.subscribe(KAFKA_TOPICS);
            LOGGER.info("Subscribe to topics {} and starting consumption of messages...", KAFKA_TOPICS);
            while (true) {
                ConsumerRecords<String, OrderEvent> records = kafkaConsumer.poll(Duration.ofMillis(200));
                records.forEach(record -> {
                    LOGGER.debug("Received message {}", record);
                    OrderEvent orderEvent = record.value();
                    if (orderEvent == null) {
                        LOGGER.debug("Order event was null");
                    } else if (OrderEventType.ORDER_COMPLETED != orderEvent.getType()) {
                        LOGGER.debug("Ignoring order event of type {}", orderEvent.getType());
                    } else {
                        LOGGER.debug("Processing order event type {}", orderEvent.getType());
                        CreateInvoice createInvoice = CreateInvoice.builder()
                                .orderId(orderEvent.getOrderId())
                                .build();
                        invoiceService.createInvoice(createInvoice);
                    }
                });
            }
        } finally {
            LOGGER.info("Unsubscribe from topics {} and ending consumption of messages...", KAFKA_TOPICS);
            kafkaConsumer.unsubscribe();
            kafkaConsumer.close();
        }
    }
}

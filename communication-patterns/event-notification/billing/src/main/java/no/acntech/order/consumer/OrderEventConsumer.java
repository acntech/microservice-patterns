package no.acntech.order.consumer;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import no.acntech.common.config.KafkaTopic;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.service.OrderService;

@SuppressWarnings("Duplicates")
@Component
public class OrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final KafkaConsumer<String, OrderEvent> kafkaConsumer;
    private final OrderService orderService;

    public OrderEventConsumer(@Qualifier("orderKafkaConsumer") final KafkaConsumer<String, OrderEvent> kafkaConsumer,
                              final OrderService orderService) {
        this.kafkaConsumer = kafkaConsumer;
        this.orderService = orderService;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Async
    public void startConsumer() {
        try {
            kafkaConsumer.subscribe(KafkaTopic.ORDERS.toList());
            LOGGER.info("Subscribe to topics {} and starting consumption of messages...", KafkaTopic.ORDERS.toList());
            while (true) {
                final ConsumerRecords<String, OrderEvent> records = kafkaConsumer.poll(Duration.ofMillis(200));
                records.forEach(this::consume);
            }
        } finally {
            LOGGER.info("Unsubscribe from topics {} and ending consumption of messages...", KafkaTopic.ORDERS.toList());
            kafkaConsumer.unsubscribe();
            kafkaConsumer.close();
        }
    }

    private void consume(final ConsumerRecord<String, OrderEvent> record) {
        final OrderEvent orderEvent = record.value();
        if (orderEvent == null) {
            LOGGER.error("Received order event which was null");
        } else {
            LOGGER.debug("Received order event with order-id {}", orderEvent.getOrderId());
            orderService.receiveOrderEvent(orderEvent);
        }
    }
}

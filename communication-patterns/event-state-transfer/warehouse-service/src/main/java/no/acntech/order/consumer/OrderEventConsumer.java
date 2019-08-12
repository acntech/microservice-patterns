package no.acntech.order.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.service.OrderService;

@Component
public class OrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventConsumer.class);
    private static final List<String> KAFKA_TOPICS = Collections.singletonList("orders");

    private final KafkaConsumer<String, OrderEvent> kafkaConsumer;
    private final OrderService orderService;

    public OrderEventConsumer(@Qualifier("orderKafkaConsumer") final KafkaConsumer<String, OrderEvent> kafkaConsumer,
                              final OrderService orderService) {
        this.kafkaConsumer = kafkaConsumer;
        this.orderService = orderService;
    }

    @SuppressWarnings({"Duplicates", "InfiniteLoopStatement"})
    @Async
    public void startConsumer() {
        try {
            kafkaConsumer.subscribe(KAFKA_TOPICS);
            LOGGER.info("Subscribe to topics {} and starting consumption of events...", KAFKA_TOPICS);
            while (true) {
                ConsumerRecords<String, OrderEvent> records = kafkaConsumer.poll(Duration.ofMillis(200));
                records.forEach(this::consume);
            }
        } finally {
            LOGGER.info("Unsubscribe from topics {} and ending consumption of events...", KAFKA_TOPICS);
            kafkaConsumer.unsubscribe();
            kafkaConsumer.close();
        }
    }

    private void consume(final ConsumerRecord<String, OrderEvent> record) {
        LOGGER.debug("Received message {}", record);
        OrderEvent orderEvent = record.value();
        orderService.receiveOrderEvent(orderEvent);
    }
}

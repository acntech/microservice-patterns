package no.acntech.reservation.consumer;

import no.acntech.order.model.ItemStatus;
import no.acntech.order.model.UpdateItem;
import no.acntech.order.service.OrderService;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationEventType;
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
public class ReservationEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationEventConsumer.class);
    private static final List<String> KAFKA_TOPICS = Collections.singletonList("reservations");

    private final KafkaConsumer<String, ReservationEvent> kafkaConsumer;
    private final OrderService orderService;

    public ReservationEventConsumer(@Qualifier("reservationKafkaConsumer") final KafkaConsumer<String, ReservationEvent> kafkaConsumer,
                                    final OrderService orderService) {
        this.kafkaConsumer = kafkaConsumer;
        this.orderService = orderService;
    }

    @SuppressWarnings("Duplicates")
    @Async
    public void startConsumer() {
        try {
            kafkaConsumer.subscribe(KAFKA_TOPICS);
            LOGGER.info("Subscribe to topics {} and starting consumption of messages...", KAFKA_TOPICS);
            while (true) {
                ConsumerRecords<String, ReservationEvent> records = kafkaConsumer.poll(Duration.ofMillis(200));
                records.forEach(record -> {
                    LOGGER.debug("Received message {}", record);
                    ReservationEvent reservationEvent = record.value();
                    if (reservationEvent == null) {
                        LOGGER.debug("Reservation event was null");
                    } else {
                        LOGGER.debug("Processing order event type {}", reservationEvent.getType());
                        ItemStatus status = ItemStatus.CONFIRMED;
                        if (ReservationEventType.PRODUCT_NOT_FOUND == reservationEvent.getType()) {
                            status = ItemStatus.REJECTED;
                        }

                        UpdateItem updateItem = UpdateItem.builder()
                                .orderId(reservationEvent.getOrderId())
                                .productId(reservationEvent.getProductId())
                                .status(status)
                                .build();
                        orderService.updateItem(updateItem);
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

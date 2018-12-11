package no.acntech.reservation.producer;

import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReservationEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationEventProducer.class);
    private static final String KAFKA_TOPIC = "reservations";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationEventProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reservationCreated(UUID orderId, UUID productId) {
        LOGGER.debug("Sending event type {} for order-id {} and product-id {} to topic {}", ReservationEventType.RESERVATION_CREATED, orderId, productId, KAFKA_TOPIC);
        kafkaTemplate.send(KAFKA_TOPIC, ReservationEvent.builder()
                .type(ReservationEventType.RESERVATION_CREATED)
                .orderId(orderId)
                .productId(productId)
                .build());
    }

    public void reservationUpdated(UUID orderId, UUID productId) {
        LOGGER.debug("Sending event type {} for order-id {} and product-id {} to topic {}", ReservationEventType.RESERVATION_UPDATED, orderId, productId, KAFKA_TOPIC);
        kafkaTemplate.send(KAFKA_TOPIC, ReservationEvent.builder()
                .type(ReservationEventType.RESERVATION_UPDATED)
                .orderId(orderId)
                .productId(productId)
                .build());
    }

    public void productNotFound(UUID orderId, UUID productId) {
        LOGGER.debug("Sending event type {} for order-id {} and product-id {} to topic {}", ReservationEventType.PRODUCT_NOT_FOUND, orderId, productId, KAFKA_TOPIC);
        kafkaTemplate.send(KAFKA_TOPIC, ReservationEvent.builder()
                .type(ReservationEventType.PRODUCT_NOT_FOUND)
                .orderId(orderId)
                .productId(productId)
                .build());
    }
}

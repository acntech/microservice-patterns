package no.acntech.order.producer;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class OrderEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String KAFKA_TOPIC = "orders";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void orderCreated(UUID orderId) {
        publish(OrderEventType.ORDER_CREATED, orderId, null, null);
    }

    @Transactional
    public void orderUpdated(UUID orderId, UUID productId, Long quantity) {
        publish(OrderEventType.ORDER_UPDATED, orderId, productId, quantity);
    }

    @Transactional
    public void orderCompleted(UUID orderId) {
        publish(OrderEventType.ORDER_COMPLETED, orderId, null, null);
    }

    @Transactional
    public void orderCanceled(UUID orderId) {
        publish(OrderEventType.ORDER_CANCELED, orderId, null, null);
    }

    @Transactional
    public void orderRejected(UUID orderId) {
        publish(OrderEventType.ORDER_REJECTED, orderId, null, null);
    }

    private void publish(OrderEventType eventType, UUID orderId, UUID productId, Long quantity) {
        LOGGER.debug("Sending event type {} for order-id {} to topic {}", eventType, orderId, KAFKA_TOPIC);
        kafkaTemplate.send(KAFKA_TOPIC, OrderEvent.builder()
                .type(eventType)
                .orderId(orderId)
                .productId(productId)
                .quantity(quantity)
                .build());
    }
}

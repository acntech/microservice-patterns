package no.acntech.order.producer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import no.acntech.order.config.KafkaTopic;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;

@Transactional
@Component
public class OrderEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void orderCreated(UUID orderId) {
        publish(OrderEventType.ORDER_CREATED, orderId);
    }

    public void orderUpdated(UUID orderId) {
        publish(OrderEventType.ORDER_UPDATED, orderId);
    }

    public void orderCompleted(UUID orderId) {
        publish(OrderEventType.ORDER_COMPLETED, orderId);
    }

    public void orderCanceled(UUID orderId) {
        publish(OrderEventType.ORDER_CANCELED, orderId);
    }

    public void orderRejected(UUID orderId) {
        publish(OrderEventType.ORDER_REJECTED, orderId);
    }

    private void publish(OrderEventType eventType, UUID orderId) {
        LOGGER.debug("Sending event type {} for order-id {} to topic {}", eventType, orderId, KafkaTopic.ORDERS.getName());
        kafkaTemplate.send(KafkaTopic.ORDERS.getName(), OrderEvent.builder()
                .type(eventType)
                .orderId(orderId)
                .build());
    }
}

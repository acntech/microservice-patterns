package no.acntech.order.producer;

import no.acntech.common.config.KafkaTopic;
import no.acntech.order.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component
public class OrderEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void publish(@NotNull final UUID orderId) {
        LOGGER.debug("Sending OrderEvent for order-id {} to topic {}", orderId, KafkaTopic.ORDERS);
        var orderEvent = OrderEvent.builder()
                .orderId(orderId)
                .build();
        kafkaTemplate.send(KafkaTopic.ORDERS, orderEvent.getEventId().toString(), orderEvent);
    }
}





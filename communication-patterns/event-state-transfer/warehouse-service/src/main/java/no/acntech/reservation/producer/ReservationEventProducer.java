package no.acntech.reservation.producer;

import javax.validation.constraints.NotNull;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import no.acntech.common.config.KafkaTopic;
import no.acntech.reservation.model.ReservationEvent;

@Component
public class ReservationEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationEventProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(@NotNull final UUID reservationId) {
        LOGGER.debug("Sending event for reservation-id {} to topic {}", reservationId, KafkaTopic.RESERVATIONS.getName());
        ReservationEvent reservationEvent = ReservationEvent.builder()
                .reservationId(reservationId)
                .build();
        kafkaTemplate.send(KafkaTopic.RESERVATIONS.getName(), reservationEvent);
    }
}

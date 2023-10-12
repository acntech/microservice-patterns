package no.acntech.reservation.consumer;

import no.acntech.common.config.KafkaTopic;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.service.ReservationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("Duplicates")
@Component
public class ReservationEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationEventConsumer.class);
    private final ReservationService reservationService;

    public ReservationEventConsumer(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @KafkaListener(topics = KafkaTopic.RESERVATIONS)
    public void consume(final ConsumerRecord<String, ReservationEvent> record) {
        final var reservationEvent = record.value();
        LOGGER.debug("Received reservation event with reservation-id {}", reservationEvent.getReservationId());
        reservationService.processReservationEvent(reservationEvent);
    }
}

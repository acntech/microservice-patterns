package no.acntech.reservation.consumer;

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
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.service.ReservationService;

@SuppressWarnings("Duplicates")
@Component
public class ReservationEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationEventConsumer.class);
    private static final Duration POLL_TIMEOUT = Duration.ofMillis(200);
    private final KafkaConsumer<String, ReservationEvent> kafkaConsumer;
    private final ReservationService reservationService;

    public ReservationEventConsumer(@Qualifier("reservationKafkaConsumer") final KafkaConsumer<String, ReservationEvent> kafkaConsumer,
                                    final ReservationService reservationService) {
        this.kafkaConsumer = kafkaConsumer;
        this.reservationService = reservationService;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Async
    public void startConsumer() {
        try {
            kafkaConsumer.subscribe(KafkaTopic.RESERVATIONS.toList());
            LOGGER.info("Subscribe to topics {} and starting consumption of messages...", KafkaTopic.RESERVATIONS.toList());
            while (true) {
                final ConsumerRecords<String, ReservationEvent> records = kafkaConsumer.poll(POLL_TIMEOUT);
                records.forEach(this::consume);
            }
        } finally {
            LOGGER.info("Unsubscribe from topics {} and ending consumption of messages...", KafkaTopic.RESERVATIONS.toList());
            kafkaConsumer.unsubscribe();
            kafkaConsumer.close();
        }
    }

    private void consume(final ConsumerRecord<String, ReservationEvent> record) {
        LOGGER.debug("Received message {}", record);
        final ReservationEvent reservationEvent = record.value();
        if (reservationEvent == null) {
            LOGGER.error("Reservation event is null");
        } else {
            LOGGER.debug("Processing reservation event for reservation-id {}", reservationEvent.getReservationId());
            reservationService.receiveReservationEvent(reservationEvent);
        }
    }
}

package no.acntech.order.consumer;

import no.acntech.order.model.OrderEvent;
import no.acntech.reservation.model.SaveReservation;
import no.acntech.reservation.service.ReservationService;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Component
public class OrderEventConsumer {

    private static final List<String> KAFKA_TOPICS = Collections.singletonList("orders");

    private final ConversionService conversionService;
    private final KafkaConsumer<String, OrderEvent> kafkaConsumer;
    private final ReservationService reservationService;

    public OrderEventConsumer(final ConversionService conversionService,
                              @Qualifier("orderKafkaConsumer") final KafkaConsumer<String, OrderEvent> kafkaConsumer,
                              final ReservationService reservationService) {
        this.conversionService = conversionService;
        this.kafkaConsumer = kafkaConsumer;
        this.reservationService = reservationService;
    }

    @PostConstruct
    public void startConsumer() {
        try {
            kafkaConsumer.subscribe(KAFKA_TOPICS);
            while (true) {
                ConsumerRecords<String, OrderEvent> records = kafkaConsumer.poll(Duration.ofMillis(200));
                records.forEach(record -> {
                    SaveReservation saveReservation = conversionService.convert(record.value(), SaveReservation.class);
                    if (saveReservation != null) {
                        reservationService.saveReservation(saveReservation);
                    }
                });
            }
        } finally {
            kafkaConsumer.unsubscribe();
            kafkaConsumer.close();
        }
    }
}

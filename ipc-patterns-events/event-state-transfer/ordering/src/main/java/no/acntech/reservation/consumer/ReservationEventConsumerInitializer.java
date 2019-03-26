package no.acntech.reservation.consumer;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ReservationEventConsumerInitializer {

    private final ReservationEventConsumer reservationEventConsumer;

    public ReservationEventConsumerInitializer(final ReservationEventConsumer reservationEventConsumer) {
        this.reservationEventConsumer = reservationEventConsumer;
    }

    @PostConstruct
    public void onStartup() {
        reservationEventConsumer.startConsumer();
    }
}

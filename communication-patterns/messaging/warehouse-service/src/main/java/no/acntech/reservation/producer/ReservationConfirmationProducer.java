package no.acntech.reservation.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import no.acntech.reservation.model.InventoryReservationConfirmationMessage;

@Component
public class ReservationConfirmationProducer {

    private final JmsTemplate jmsTemplate;
    private final String reservationConfirmationQueue;

    @Autowired
    public ReservationConfirmationProducer(JmsTemplate jmsTemplate,
                                           @Value("${queue.warehouse.reservation.confirmation}") String reservationConfirmationQueue) {
        this.jmsTemplate = jmsTemplate;
        this.reservationConfirmationQueue = reservationConfirmationQueue;
    }

    public void sendConfirmation(InventoryReservationConfirmationMessage message) {
        jmsTemplate.convertAndSend(reservationConfirmationQueue, message);
    }
}

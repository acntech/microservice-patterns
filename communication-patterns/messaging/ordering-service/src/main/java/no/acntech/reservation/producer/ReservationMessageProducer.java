package no.acntech.reservation.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import no.acntech.order.model.Order;
import no.acntech.reservation.model.InventoryReservationMessage;

@Component
public class ReservationMessageProducer {

    private final JmsTemplate jmsTemplate;
    private final String warehouseReservationQueue;

    @Autowired
    public ReservationMessageProducer(JmsTemplate jmsTemplate,
                                      @Value("${queue.warehouse.reservation.reserve}") String warehouseReservationQueue) {
        this.jmsTemplate = jmsTemplate;
        this.warehouseReservationQueue = warehouseReservationQueue;
    }

    public void reserve(Order order) {
        InventoryReservationMessage message = new InventoryReservationMessage(order.getId(), order.orderlinesAsMap());
        jmsTemplate.convertAndSend(warehouseReservationQueue, message);
    }
}

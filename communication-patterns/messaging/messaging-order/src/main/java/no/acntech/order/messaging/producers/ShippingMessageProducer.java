package no.acntech.order.messaging.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import no.acntech.messaging.types.shipping.SendShipmentMessage;

@Component
public class ShippingMessageProducer {

    private final JmsTemplate jmsTemplate;
    private final String shippingQueue;

    @Autowired
    public ShippingMessageProducer(JmsTemplate jmsTemplate,
                                   @Value("${queue.shipping.shipment.send}") String shippingQueue) {
        this.jmsTemplate = jmsTemplate;
        this.shippingQueue = shippingQueue;
    }

    public void ship(Long orderId, String reservationId) {
        SendShipmentMessage sendShipmentMessage = new SendShipmentMessage(orderId, reservationId);
        jmsTemplate.convertAndSend(shippingQueue, sendShipmentMessage);
    }
}

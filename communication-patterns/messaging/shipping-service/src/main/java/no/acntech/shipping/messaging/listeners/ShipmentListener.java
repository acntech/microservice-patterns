package no.acntech.shipping.messaging.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import no.acntech.messaging.types.shipping.SendShipmentMessage;

@Component
public class ShipmentListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentListener.class);

    @JmsListener(destination = "${queue.shipping.shipment.send}")
    public void reserve(SendShipmentMessage sendShipmentMessage) {
        LOGGER.info("Shipment sent! orderId={} reservationId={}", sendShipmentMessage.getOrderId(), sendShipmentMessage.getReservationId());
    }
}

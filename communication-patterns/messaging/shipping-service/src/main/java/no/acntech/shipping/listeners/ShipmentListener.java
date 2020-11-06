package no.acntech.shipping.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import no.acntech.shipping.model.SendShipmentMessage;

@Component
public class ShipmentListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentListener.class);

    @JmsListener(destination = "${queue.shipping.shipment.send}")
    public void reserve(SendShipmentMessage message) {
        LOGGER.info("Shipment sent! orderId={} reservationId={}", message.getOrderId(), message.getReservationId());
    }
}

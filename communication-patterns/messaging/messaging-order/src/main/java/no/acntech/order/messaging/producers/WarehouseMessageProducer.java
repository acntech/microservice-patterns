package no.acntech.order.messaging.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import no.acntech.messaging.types.warehouse.InventoryReservationMessage;
import no.acntech.order.entity.Order;

@Component
public class WarehouseMessageProducer {

    private final JmsTemplate jmsTemplate;
    private final String warehouseReservationQueue;

    @Autowired
    public WarehouseMessageProducer(JmsTemplate jmsTemplate,
                                    @Value("${queue.warehouse.reservation.reserve}") String warehouseReservationQueue) {
        this.jmsTemplate = jmsTemplate;
        this.warehouseReservationQueue = warehouseReservationQueue;
    }

    public void reserve(Order order) {
        InventoryReservationMessage inventoryReservationMessage = new InventoryReservationMessage(order.getId(), order.orderlinesAsMap());
        jmsTemplate.convertAndSend(warehouseReservationQueue, inventoryReservationMessage);
    }
}

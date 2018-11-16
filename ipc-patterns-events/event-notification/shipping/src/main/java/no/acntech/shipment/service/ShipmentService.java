package no.acntech.shipment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.repository.ShipmentRepository;

@Service
public class ShipmentService {


    private final ShipmentRepository shipmentRepository;

    public ShipmentService(final ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public List<Shipment> findOrders() {
        return shipmentRepository.findAll();
    }
}

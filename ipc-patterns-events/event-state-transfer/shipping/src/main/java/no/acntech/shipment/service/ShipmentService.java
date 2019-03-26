package no.acntech.shipment.service;

import no.acntech.shipment.exception.ShipmentNotFoundException;
import no.acntech.shipment.model.CreateShipment;
import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.repository.ShipmentRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShipmentService {

    private final ConversionService conversionService;
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(final ConversionService conversionService,
                           final ShipmentRepository shipmentRepository) {
        this.conversionService = conversionService;
        this.shipmentRepository = shipmentRepository;
    }

    @SuppressWarnings("Duplicates")
    public List<Shipment> findShipments(final ShipmentQuery shipmentQuery) {
        UUID orderId = shipmentQuery.getOrderId();
        Shipment.Status status = shipmentQuery.getStatus();
        if (orderId != null && status != null) {
            return shipmentRepository.findAllByOrderIdAndStatus(orderId, status);
        } else if (orderId != null) {
            return shipmentRepository.findAllByOrderId(orderId);
        } else if (status != null) {
            return shipmentRepository.findAllByStatus(status);
        } else {
            return shipmentRepository.findAll(Sort.by("id"));
        }
    }

    public Shipment getShipment(final UUID shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException(shipmentId));
    }

    public Shipment createShipment(final CreateShipment createShipment) {
        Shipment shipment = conversionService.convert(createShipment, Shipment.class);
        return shipmentRepository.save(shipment);
    }
}

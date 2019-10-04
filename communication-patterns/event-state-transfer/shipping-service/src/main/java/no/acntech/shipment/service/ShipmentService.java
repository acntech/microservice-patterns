package no.acntech.shipment.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import no.acntech.shipment.exception.ShipmentNotFoundException;
import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.model.ShipmentDto;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.model.ShipmentStatus;
import no.acntech.shipment.repository.ShipmentRepository;

@Service
public class ShipmentService {

    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(final ConversionService conversionService,
                           final ShipmentRepository shipmentRepository) {
        this.conversionService = conversionService;
        this.shipmentRepository = shipmentRepository;
    }

    @SuppressWarnings("Duplicates")
    public List<ShipmentDto> findShipments(final ShipmentQuery shipmentQuery) {
        UUID customerId = shipmentQuery.getCustomerId();
        UUID orderId = shipmentQuery.getOrderId();
        ShipmentStatus status = shipmentQuery.getStatus();
        if (customerId != null && orderId != null && status != null) {
            return shipmentRepository.findAllByCustomerIdAndOrderIdAndStatus(customerId, orderId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (customerId != null && status != null) {
            return shipmentRepository.findAllByCustomerIdAndStatus(customerId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (orderId != null && status != null) {
            return shipmentRepository.findAllByOrderIdAndStatus(orderId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (customerId != null) {
            return shipmentRepository.findAllByCustomerId(customerId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (orderId != null) {
            return shipmentRepository.findAllByOrderId(orderId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return shipmentRepository.findAllByStatus(status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return shipmentRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public ShipmentDto getShipment(final UUID shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId)
                .map(this::convert)
                .orElseThrow(() -> new ShipmentNotFoundException(shipmentId));
    }

    public ShipmentDto createShipment(final CreateShipmentDto createShipment) {
        Shipment shipment = conversionService.convert(createShipment, Shipment.class);
        Assert.notNull(shipment, "Failed to convert shipment");

        Shipment savedShipment = shipmentRepository.save(shipment);
        return convert(savedShipment);
    }

    private ShipmentDto convert(Shipment shipment) {
        return conversionService.convert(shipment, ShipmentDto.class);
    }
}

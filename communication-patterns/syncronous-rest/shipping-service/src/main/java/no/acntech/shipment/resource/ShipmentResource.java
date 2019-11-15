package no.acntech.shipment.resource;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import no.acntech.shipment.model.CreateShipment;
import no.acntech.shipment.model.ShipmentDto;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.service.ShipmentService;

@RequestMapping(path = "shipments")
@RestController
public class ShipmentResource {

    private final ShipmentService shipmentService;

    public ShipmentResource(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDto>> get(final ShipmentQuery shipmentQuery) {
        List<ShipmentDto> shipments = shipmentService.findShipments(shipmentQuery);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping(path = "{shipmentId}")
    public ResponseEntity<ShipmentDto> get(@PathVariable("shipmentId") final UUID shipmentId) {
        ShipmentDto shipment = shipmentService.getShipment(shipmentId);
        return ResponseEntity.ok(shipment);
    }

    @PostMapping
    public ResponseEntity createShipment(final CreateShipment createShipment) {
        final ShipmentDto shipment = shipmentService.createShipment(createShipment);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{shipmentId}")
                .buildAndExpand(shipment.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

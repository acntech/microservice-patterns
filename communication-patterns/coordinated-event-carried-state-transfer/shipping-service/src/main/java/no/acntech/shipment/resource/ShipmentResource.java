package no.acntech.shipment.resource;

import no.acntech.shipment.model.ShipmentDto;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/shipments")
@RestController
public class ShipmentResource {

    private final ShipmentService shipmentService;

    public ShipmentResource(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDto>> get(final ShipmentQuery shipmentQuery) {
        final var shipmentDtos = shipmentService.findShipments(shipmentQuery);
        return ResponseEntity.ok(shipmentDtos);
    }

    @GetMapping(path = "{shipmentId}")
    public ResponseEntity<ShipmentDto> get(@PathVariable("shipmentId") final UUID shipmentId) {
        final var shipmentDto = shipmentService.getShipment(shipmentId);
        return ResponseEntity.ok(shipmentDto);
    }
}

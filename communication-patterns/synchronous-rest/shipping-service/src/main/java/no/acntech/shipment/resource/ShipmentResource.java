package no.acntech.shipment.resource;

import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.model.ShipmentDto;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<ShipmentDto> create(final CreateShipmentDto createShipmentDto) {
        final var shipmentDto = shipmentService.createShipment(createShipmentDto);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{shipmentId}")
                .buildAndExpand(shipmentDto.getCustomerId())
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }
}

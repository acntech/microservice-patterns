package no.acntech.shipment.resource;

import no.acntech.shipment.model.CreateShipment;
import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "shipments")
@RestController
public class ShipmentResource {

    private final ShipmentService shipmentService;

    public ShipmentResource(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public List<Shipment> get(final ShipmentQuery shipmentQuery) {
        return shipmentService.findShipments(shipmentQuery);
    }

    @GetMapping(path = "{shipmentId}")
    public ResponseEntity<Shipment> get(@Valid @NotNull @PathVariable("shipmentId") final UUID shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipment(shipmentId));
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody final CreateShipment createShipment) {
        Shipment shipment = shipmentService.createShipment(createShipment);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{shipmentId}")
                .buildAndExpand(shipment.getShipmentId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

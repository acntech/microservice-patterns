package no.acntech.shipment.resource;

import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
}

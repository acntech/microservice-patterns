package no.acntech.shipment.resource;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.service.ShipmentService;

@RequestMapping(path = "shipments")
@RestController
public class ShipmentResource {

    private final ShipmentService shipmentService;

    public ShipmentResource(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public List<Shipment> get() {
        return shipmentService.findOrders();
    }
}

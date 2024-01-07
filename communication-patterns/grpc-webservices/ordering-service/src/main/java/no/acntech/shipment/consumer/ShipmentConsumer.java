package no.acntech.shipment.consumer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import no.acntech.shipment.endpoint.ShipmentEndpointGrpc;
import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.model.CreateShipmentRequest;
import no.acntech.shipment.model.ShipmentDto;
import org.springframework.stereotype.Component;

@Component
public class ShipmentConsumer {

    @GrpcClient("shipping-service")
    private ShipmentEndpointGrpc.ShipmentEndpointBlockingStub blockingStub;

    public ShipmentDto createShipment(@NotNull @Valid final CreateShipmentDto createShipmentDto) {
        final var request = CreateShipmentRequest.newBuilder()
                .setBody(createShipmentDto)
                .build();
        final var response = blockingStub.createShipment(request);
        return response.getBody();
    }
}

package no.acntech.shipment.endpoint;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import no.acntech.common.error.GrpcErrorHandler;
import no.acntech.shipment.model.*;
import no.acntech.shipment.service.ShipmentService;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.util.UUID;

@GrpcService
public class ShipmentEndpoint extends ShipmentEndpointGrpc.ShipmentEndpointImplBase {

    private final ConversionService conversionService;
    private final GrpcErrorHandler errorHandler;
    private final ShipmentService shipmentService;

    public ShipmentEndpoint(final ConversionService conversionService,
                            final GrpcErrorHandler errorHandler,
                            final ShipmentService shipmentService) {
        this.conversionService = conversionService;
        this.errorHandler = errorHandler;
        this.shipmentService = shipmentService;
    }

    @Override
    public void getShipment(final GetShipmentRequest request,
                            final StreamObserver<GetShipmentResponse> responseObserver) {
        try {
            final var shipmentDto = shipmentService.getShipment(UUID.fromString(request.getHeader().getShipmentId().getValue()));
            final var response = GetShipmentResponse.newBuilder()
                    .setBody(shipmentDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void findShipments(final FindShipmentsRequest request,
                              final StreamObserver<FindShipmentsResponse> responseObserver) {
        try {
            final var query = conversionService.convert(request, ShipmentQuery.class);
            Assert.notNull(query, "Failed to convert FindShipmentsRequest to ShipmentQuery");
            final var shipmentDtos = shipmentService.findShipments(query);
            final var response = FindShipmentsResponse.newBuilder()
                    .addAllBody(shipmentDtos)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void createShipment(final CreateShipmentRequest request,
                               final StreamObserver<CreateShipmentResponse> responseObserver) {
        try {
            final var shipmentDto = shipmentService.createShipment(request.getBody());
            final var response = CreateShipmentResponse.newBuilder()
                    .setBody(shipmentDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }
}

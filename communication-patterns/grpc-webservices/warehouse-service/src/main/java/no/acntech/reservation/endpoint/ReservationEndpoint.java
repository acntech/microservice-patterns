package no.acntech.reservation.endpoint;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import no.acntech.common.error.GrpcErrorHandler;
import no.acntech.reservation.model.*;
import no.acntech.reservation.service.ReservationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.util.UUID;

@GrpcService
public class ReservationEndpoint extends ReservationEndpointGrpc.ReservationEndpointImplBase {

    private final ConversionService conversionService;
    private final GrpcErrorHandler errorHandler;
    private final ReservationService reservationService;

    public ReservationEndpoint(final ConversionService conversionService,
                               final GrpcErrorHandler errorHandler,
                               final ReservationService reservationService) {
        this.conversionService = conversionService;
        this.errorHandler = errorHandler;
        this.reservationService = reservationService;
    }

    @Override
    public void getReservation(final GetReservationRequest request,
                               final StreamObserver<GetReservationResponse> responseObserver) {
        try {
            final var reservationDto = reservationService.getReservation(UUID.fromString(request.getHeader().getReservationId().getValue()));
            final var response = GetReservationResponse.newBuilder()
                    .setBody(reservationDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void findReservations(final FindReservationsRequest request,
                                 final StreamObserver<FindReservationsResponse> responseObserver) {
        try {
            final var query = conversionService.convert(request, ReservationQuery.class);
            Assert.notNull(query, "Failed to convert FindOrderItemsRequest to OrderItemQuery");
            final var reservationDtos = reservationService.findReservations(query);
            final var response = FindReservationsResponse.newBuilder()
                    .addAllBody(reservationDtos)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void createReservation(final CreateReservationRequest request,
                                  final StreamObserver<CreateReservationResponse> responseObserver) {
        try {
            final var reservationDto = reservationService.createReservation(request.getBody());
            final var response = CreateReservationResponse.newBuilder()
                    .setBody(reservationDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void updateReservation(final UpdateReservationRequest request,
                                  final StreamObserver<UpdateReservationResponse> responseObserver) {
        try {
            final var reservationDto = reservationService.updateReservation(
                    UUID.fromString(request.getHeader().getReservationId().getValue()),
                    request.getBody());
            final var response = UpdateReservationResponse.newBuilder()
                    .setBody(reservationDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void deleteReservation(final DeleteReservationRequest request,
                                  final StreamObserver<DeleteReservationResponse> responseObserver) {
        try {
            final var reservationDto = reservationService.deleteReservation(
                    UUID.fromString(request.getHeader().getReservationId().getValue()));
            final var response = DeleteReservationResponse.newBuilder()
                    .setBody(reservationDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }
}

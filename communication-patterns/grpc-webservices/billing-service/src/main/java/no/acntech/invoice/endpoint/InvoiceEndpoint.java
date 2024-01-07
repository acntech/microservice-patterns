package no.acntech.invoice.endpoint;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import no.acntech.common.error.GrpcErrorHandler;
import no.acntech.invoice.model.*;
import no.acntech.invoice.service.InvoiceService;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.util.UUID;

@GrpcService
public class InvoiceEndpoint extends InvoiceEndpointGrpc.InvoiceEndpointImplBase {

    private final ConversionService conversionService;
    private final GrpcErrorHandler errorHandler;
    private final InvoiceService invoiceService;

    public InvoiceEndpoint(final ConversionService conversionService,
                           final GrpcErrorHandler errorHandler,
                           final InvoiceService invoiceService) {
        this.conversionService = conversionService;
        this.errorHandler = errorHandler;
        this.invoiceService = invoiceService;
    }

    @Override
    public void getInvoice(final GetInvoiceRequest request,
                           final StreamObserver<GetInvoiceResponse> responseObserver) {
        try {
            final var invoiceDto = invoiceService.getInvoice(UUID.fromString(request.getHeader().getInvoiceId().getValue()));
            final var response = GetInvoiceResponse.newBuilder()
                    .setBody(invoiceDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void findInvoices(final FindInvoicesRequest request,
                             final StreamObserver<FindInvoicesResponse> responseObserver) {
        try {
            final var query = conversionService.convert(request, InvoiceQuery.class);
            Assert.notNull(query, "Failed to convert FindInvoicesRequest to InvoiceQuery");
            final var invoiceDtos = invoiceService.findInvoices(query);
            final var response = FindInvoicesResponse.newBuilder()
                    .addAllBody(invoiceDtos)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void createInvoice(final CreateInvoiceRequest request,
                              final StreamObserver<CreateInvoiceResponse> responseObserver) {
        try {
            final var invoiceDto = invoiceService.createInvoice(request.getBody());
            final var response = CreateInvoiceResponse.newBuilder()
                    .setBody(invoiceDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }
}

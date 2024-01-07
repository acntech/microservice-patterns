package no.acntech.invoice.consumer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import no.acntech.invoice.endpoint.InvoiceEndpointGrpc;
import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.model.CreateInvoiceRequest;
import no.acntech.invoice.model.InvoiceDto;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsumer {

    @GrpcClient("billing-service")
    private InvoiceEndpointGrpc.InvoiceEndpointBlockingStub blockingStub;

    public InvoiceDto createInvoice(@NotNull @Valid final CreateInvoiceDto createInvoiceDto) {
        final var request = CreateInvoiceRequest.newBuilder()
                .setBody(createInvoiceDto)
                .build();
        final var response = blockingStub.createInvoice(request);
        return response.getBody();
    }
}

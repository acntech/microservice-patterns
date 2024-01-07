package no.acntech.invoice.converter;

import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import no.acntech.invoice.model.InvoiceDto;
import no.acntech.invoice.model.InvoiceEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class InvoiceEntityToInvoiceDtoConverter implements Converter<InvoiceEntity, InvoiceDto> {

    @NonNull
    @Override
    public InvoiceDto convert(@NonNull InvoiceEntity source) {
        return InvoiceDto.newBuilder()
                .setInvoiceId(StringValue.of(source.getInvoiceId().toString()))
                .setCustomerId(StringValue.of(source.getCustomerId().toString()))
                .setOrderId(StringValue.of(source.getOrderId().toString()))
                .setStatus(source.getStatus())
                .setCreated(Timestamp.newBuilder()
                        .setSeconds(source.getCreated().toInstant().getEpochSecond())
                        .setNanos(source.getCreated().toInstant().getNano())
                        .build())
                .setModified(source.getModified() != null ? Timestamp.newBuilder()
                        .setSeconds(source.getModified().toInstant().getEpochSecond())
                        .setNanos(source.getModified().toInstant().getNano())
                        .build() : Timestamp.getDefaultInstance())
                .build();
    }
}

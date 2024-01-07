package no.acntech.shipment.converter;

import no.acntech.shipment.model.FindShipmentsRequest;
import no.acntech.shipment.model.ShipmentQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindShipmentsRequestToShipmentQueryConverter implements Converter<FindShipmentsRequest, ShipmentQuery> {

    @NonNull
    @Override
    public ShipmentQuery convert(@NonNull final FindShipmentsRequest source) {
        return ShipmentQuery.builder()
                .customerId(source.getHeader().hasCustomerId() ? UUID.fromString(source.getHeader().getCustomerId().getValue()) : null)
                .orderId(source.getHeader().hasOrderId() ? UUID.fromString(source.getHeader().getOrderId().getValue()) : null)
                .status(source.getHeader().hasStatus() ? source.getHeader().getStatus() : null)
                .build();
    }
}

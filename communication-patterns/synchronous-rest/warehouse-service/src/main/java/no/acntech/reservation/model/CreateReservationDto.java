package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class CreateReservationDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    @Min(1)
    @NotNull
    private Long quantity;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}

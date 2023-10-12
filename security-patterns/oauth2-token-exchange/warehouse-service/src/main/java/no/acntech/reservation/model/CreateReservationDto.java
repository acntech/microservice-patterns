package no.acntech.reservation.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

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

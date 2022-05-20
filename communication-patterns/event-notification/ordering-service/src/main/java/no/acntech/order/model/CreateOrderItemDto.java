package no.acntech.order.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CreateOrderItemDto {

    @NotNull
    private UUID productId;
    @Min(1)
    @NotNull
    private Long quantity;
    private UUID reservationId;

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }
}

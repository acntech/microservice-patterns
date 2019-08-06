package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Valid
public class ItemDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    private UUID reservationId;
    @NotNull
    private Long quantity;
    @NotNull
    private ItemStatus status;
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }
}

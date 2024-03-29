package no.acntech.order.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Valid
public class OrderItemDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    private UUID reservationId;
    @NotNull
    private Long quantity;
    @NotNull
    private OrderItemStatus status;
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

    public OrderItemStatus getStatus() {
        return status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }
}

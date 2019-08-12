package no.acntech.reservation.model;

import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ReservationEvent {

    @NotNull
    private UUID reservationId;
    @NotNull
    private UUID productId;
    @NotNull
    private UUID orderId;
    @NotNull
    private Long quantity;
    private ReservationStatus status;
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public UUID getReservationId() {
        return reservationId;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }
}

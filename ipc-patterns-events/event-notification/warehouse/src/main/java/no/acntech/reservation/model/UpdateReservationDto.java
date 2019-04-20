package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Valid
public class UpdateReservationDto {

    @NotNull
    private UUID orderId;
    private UUID productId;
    private Long quantity;
    private ReservationStatus status;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    @JsonIgnore
    @AssertTrue
    public boolean isValid() {
        return isValidUpdateQuantity() || isValidUpdateStatus();
    }

    public boolean isValidUpdateQuantity() {
        return productId != null && quantity != null;
    }

    public boolean isValidUpdateStatus() {
        return status != null && (status == ReservationStatus.CANCELED || status == ReservationStatus.REJECTED);
    }
}

package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Valid
public class UpdateItemDto {

    @NotNull
    private UUID productId;
    private Long quantity;
    private ItemStatus status;

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ItemStatus getStatus() {
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

    private boolean isValidUpdateStatus() {
        return status != null && status == ItemStatus.CANCELED;
    }
}

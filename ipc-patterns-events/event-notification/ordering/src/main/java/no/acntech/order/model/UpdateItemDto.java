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

    public static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    @AssertTrue
    public boolean isValid() {
        return quantity != null || status != null;
    }

    public static final class Builder {

        private UUID productId;
        private Long quantity;
        private ItemStatus status;

        private Builder() {
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder status(ItemStatus status) {
            this.status = status;
            return this;
        }

        public UpdateItemDto build() {
            UpdateItemDto updateItem = new UpdateItemDto();
            updateItem.productId = this.productId;
            updateItem.status = this.status;
            updateItem.quantity = this.quantity;
            return updateItem;
        }
    }
}

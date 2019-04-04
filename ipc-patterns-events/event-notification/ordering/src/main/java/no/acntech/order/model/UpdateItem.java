package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class UpdateItem {

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

        public UpdateItem build() {
            UpdateItem updateItem = new UpdateItem();
            updateItem.productId = this.productId;
            updateItem.status = this.status;
            updateItem.quantity = this.quantity;
            return updateItem;
        }
    }
}

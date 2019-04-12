package no.acntech.order.model;

import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ItemDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
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

    public Long getQuantity() {
        return quantity;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }
}

package no.acntech.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "ITEMS")
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long orderId;
    @NotNull
    private UUID productId;
    @NotNull
    private Long quantity;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    private ZonedDateTime created;
    private ZonedDateTime modified;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public Long getOrderId() {
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

    @PrePersist
    public void prePersist() {
        status = ItemStatus.PENDING;
        created = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modified = ZonedDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Long orderId;
        private UUID productId;
        private Long quantity;

        private Builder() {
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Item build() {
            Item item = new Item();
            item.orderId = this.orderId;
            item.productId = this.productId;
            item.quantity = this.quantity;
            return item;
        }
    }
}

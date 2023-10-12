package no.acntech.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "ORDER_ITEMS")
@Entity
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UUID itemId;
    @Column(nullable = false)
    private UUID productId;
    @Column
    private UUID reservationId;
    @Column(nullable = false)
    private Long quantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderItemStatus status;
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    @Column
    private ZonedDateTime modified;
    @ManyToOne(optional = false)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity parent;

    public Long getId() {
        return id;
    }

    public UUID getItemId() {
        return itemId;
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

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public OrderEntity getParent() {
        return parent;
    }

    public void setParent(OrderEntity parent) {
        this.parent = parent;
    }

    public void statusConfirmed() {
        status = OrderItemStatus.CONFIRMED;
    }

    public void statusCanceled() {
        status = OrderItemStatus.CANCELED;
    }

    @PrePersist
    private void prePersist() {
        itemId = UUID.randomUUID();
        if (status == null) {
            status = OrderItemStatus.PENDING;
        }
        created = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modified = ZonedDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID productId;
        private UUID reservationId;
        private Long quantity;
        private OrderItemStatus status;

        private Builder() {
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder reservationId(UUID reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder status(OrderItemStatus status) {
            this.status = status;
            return this;
        }

        public OrderItemEntity build() {
            final var target = new OrderItemEntity();
            target.productId = this.productId;
            target.reservationId = this.reservationId;
            target.quantity = this.quantity;
            target.status = this.status;
            return target;
        }
    }
}

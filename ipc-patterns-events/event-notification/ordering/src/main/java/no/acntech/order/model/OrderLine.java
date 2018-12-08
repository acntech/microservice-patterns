package no.acntech.order.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "ORDER_LINES")
@Entity
public class OrderLine {

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
    private OrderLineStatus status;
    private ZonedDateTime created;
    private ZonedDateTime modified;

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

    public OrderLineStatus getStatus() {
        return status;
    }

    public void setStatus(OrderLineStatus status) {
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
        status = OrderLineStatus.PENDING;
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

        public OrderLine build() {
            OrderLine orderLine = new OrderLine();
            orderLine.orderId = this.orderId;
            orderLine.productId = this.productId;
            orderLine.quantity = this.quantity;
            return orderLine;
        }
    }
}

package no.acntech.order.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "ORDERS")
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UUID orderId;
    @Column(nullable = false)
    private UUID customerId;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @SortNatural
    @Where(clause = "status != 'CANCELED'")
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItemEntity> items = new ArrayList<>();
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    @Column
    private ZonedDateTime modified;

    public Long getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public void addItem(OrderItemEntity orderItemEntity) {
        orderItemEntity.setParent(this);
        items.add(orderItemEntity);
    }

    public void setStatusConfirmed() {
        status = OrderStatus.CLOSED;
    }

    public void setStatusCanceled() {
        status = OrderStatus.CANCELED;
    }

    public boolean areAllItemsReserved() {
        return items.stream()
                .filter(OrderItemEntity::isNotCanceled)
                .allMatch(OrderItemEntity::isReserved);
    }

    @PrePersist
    private void prePersist() {
        orderId = UUID.randomUUID();
        status = OrderStatus.OPEN;
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

        private UUID customerId;
        private String name;
        private String description;

        private Builder() {
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public OrderEntity build() {
            final var target = new OrderEntity();
            target.customerId = this.customerId;
            target.name = this.name;
            target.description = this.description;
            return target;
        }
    }
}

package no.acntech.order.model;

import org.hibernate.annotations.SortNatural;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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

    public void statusConfirmed() {
        status = OrderStatus.CONFIRMED;
    }

    public void statusCanceled() {
        status = OrderStatus.CANCELED;
    }

    public boolean areAllItemsReserved() {
        List<OrderItemEntity> activeItems = items.stream()
                .filter(activeItem -> !OrderItemStatus.CANCELED.equals(activeItem.getStatus()))
                .toList();
        boolean allActiveItemsConfirmed = activeItems.stream()
                .map(OrderItemEntity::getStatus)
                .allMatch(OrderItemStatus.RESERVED::equals);
        return !activeItems.isEmpty() && allActiveItemsConfirmed;
    }

    @PrePersist
    private void prePersist() {
        orderId = UUID.randomUUID();
        status = OrderStatus.PENDING;
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

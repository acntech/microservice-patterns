package no.acntech.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID customerId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private OrderStatus status;
    @NotNull
    private List<OrderItemDto> items;
    @NotNull
    private ZonedDateTime modified;
    private ZonedDateTime created;

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

    public List<OrderItemDto> getItems() {
        return items;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    @JsonIgnore
    public boolean hasItemWithProductId(UUID productId) {
        return items.stream()
                .map(OrderItemDto::getProductId)
                .anyMatch(productId::equals);
    }

    @JsonIgnore
    public boolean areAllItemsConfirmed() {
        List<OrderItemDto> activeItems = items.stream()
                .filter(activeItem -> !OrderItemStatus.DELETED.equals(activeItem.getStatus()))
                .toList();
        boolean allActiveItemsConfirmed = activeItems.stream()
                .map(OrderItemDto::getStatus)
                .allMatch(OrderItemStatus.CONFIRMED::equals);
        return !activeItems.isEmpty() && allActiveItemsConfirmed;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID customerId;
        private String name;
        private String description;
        private OrderStatus status;
        private List<OrderItemDto> items;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
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

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder items(List<OrderItemDto> items) {
            this.items = items;
            return this;
        }

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(ZonedDateTime modified) {
            this.modified = modified;
            return this;
        }

        public OrderDto build() {
            final var target = new OrderDto();
            target.customerId = this.customerId;
            target.orderId = this.orderId;
            target.name = this.name;
            target.description = this.description;
            target.status = this.status;
            target.modified = this.modified;
            target.items = this.items;
            target.created = this.created;
            return target;
        }
    }
}

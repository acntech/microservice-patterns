package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Valid
public class OrderDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID customerId;
    @NotNull
    private OrderStatus status;
    @NotNull
    private List<ItemDto> items;
    private ZonedDateTime created;
    @NotNull
    private ZonedDateTime modified;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID customerId;
        private OrderStatus status;
        private List<ItemDto> items;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public static Builder anOrderDto() {
            return new Builder();
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder items(List<ItemDto> items) {
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
            OrderDto orderDto = new OrderDto();
            orderDto.customerId = this.customerId;
            orderDto.orderId = this.orderId;
            orderDto.status = this.status;
            orderDto.modified = this.modified;
            orderDto.items = this.items;
            orderDto.created = this.created;
            return orderDto;
        }
    }
}

package no.acntech.order.model;

import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

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

    public void setStatus(OrderStatus status) {
        this.status = status;
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
}

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
    private String name;
    private String description;
    @NotNull
    private OrderStatus status;
    @NotNull
    private List<ItemDto> items;
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

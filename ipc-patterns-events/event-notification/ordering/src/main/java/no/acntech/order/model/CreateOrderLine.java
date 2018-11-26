package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class CreateOrderLine {

    private Long orderId;
    @NotNull
    private UUID productId;
    @Min(1)
    @NotNull
    private Long quantity;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}

package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Valid
public class OrderEvent implements Serializable {

    @NotNull
    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;

        private Builder() {
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderEvent build() {
            OrderEvent orderEvent = new OrderEvent();
            orderEvent.orderId = this.orderId;
            return orderEvent;
        }
    }
}

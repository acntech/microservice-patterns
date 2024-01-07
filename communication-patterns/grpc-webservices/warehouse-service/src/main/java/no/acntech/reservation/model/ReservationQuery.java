package no.acntech.reservation.model;

import java.util.UUID;

public class ReservationQuery {

    private UUID orderId;

    private ReservationQuery() {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public static Builder build() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;

        private Builder() {
        }

        public Builder orderId(UUID customerId) {
            this.orderId = customerId;
            return this;
        }

        public ReservationQuery build() {
            final var target = new ReservationQuery();
            target.orderId = this.orderId;
            return target;
        }
    }
}

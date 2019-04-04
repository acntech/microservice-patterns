package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class CreateOrder {

    @NotNull
    private UUID customerId;

    public UUID getCustomerId() {
        return customerId;
    }
}

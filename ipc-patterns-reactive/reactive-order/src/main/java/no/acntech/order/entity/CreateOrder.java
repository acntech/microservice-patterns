package no.acntech.order.entity;

import javax.validation.constraints.NotNull;

public class CreateOrder {

    @NotNull
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }
}

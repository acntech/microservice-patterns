package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Valid
public class UpdateOrderDto {

    @NotNull
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    @SuppressWarnings("Duplicates")
    @JsonIgnore
    @AssertTrue
    public boolean isValid() {
        switch (status) {
            case CONFIRMED:
            case CANCELED:
                return Boolean.TRUE;
            default:
                return Boolean.FALSE;

        }
    }
}

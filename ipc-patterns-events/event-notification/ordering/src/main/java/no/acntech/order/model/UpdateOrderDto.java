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

    @JsonIgnore
    @AssertTrue
    public boolean isValidStatus() {
        switch (status) {
            case COMPLETED:
            case CANCELED:
            case REJECTED:
                return Boolean.TRUE;
            default:
                return Boolean.FALSE;

        }
    }
}

package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
public class UpdateItemDto {

    @NotNull
    private Long quantity;

    public Long getQuantity() {
        return quantity;
    }
}

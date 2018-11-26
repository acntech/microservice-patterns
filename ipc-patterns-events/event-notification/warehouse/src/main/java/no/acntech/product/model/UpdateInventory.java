package no.acntech.product.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
public class UpdateInventory {

    @NotNull(message = "Quantity is null")
    private Long quantity;

    public Long getQuantity() {
        return quantity;
    }
}

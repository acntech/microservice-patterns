package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class UpdateItemDto {

    @NotNull
    private UUID productId;
    @NotNull
    private Long quantity;

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}

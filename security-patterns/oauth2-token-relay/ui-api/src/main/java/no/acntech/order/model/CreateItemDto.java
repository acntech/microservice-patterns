package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class CreateItemDto {

    @NotNull
    private UUID productId;
    @Min(1)
    @NotNull
    private Long quantity;

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}

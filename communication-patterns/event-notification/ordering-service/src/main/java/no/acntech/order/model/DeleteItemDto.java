package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class DeleteItemDto {

    @NotNull
    private UUID productId;

    public UUID getProductId() {
        return productId;
    }
}

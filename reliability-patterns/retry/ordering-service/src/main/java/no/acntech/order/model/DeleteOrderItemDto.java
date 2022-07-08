package no.acntech.order.model;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class DeleteOrderItemDto {

    @NotNull
    private UUID productId;

    public UUID getProductId() {
        return productId;
    }
}

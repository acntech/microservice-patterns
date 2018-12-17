package no.acntech.order.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateItem {

    @NotNull
    private String productId;
    @Min(1)
    @NotNull
    private Integer quantity;

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

}

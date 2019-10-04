package no.acntech.product.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Valid
public class CreateProductDto {

    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long stock;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Currency currency;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getStock() {
        return stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }
}

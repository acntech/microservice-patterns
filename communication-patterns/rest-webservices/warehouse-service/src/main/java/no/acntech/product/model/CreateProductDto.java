package no.acntech.product.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateProductDto {

    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long stock;
    @NotNull
    private Packaging packaging;
    @NotNull
    private Integer quantity;
    @NotNull
    private Measure measure;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Currency currency;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getStock() {
        return stock;
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Measure getMeasure() {
        return measure;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }
}

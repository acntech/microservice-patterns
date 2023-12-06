package no.acntech.product.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ProductDto {

    @NotNull
    private UUID productId;
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
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public UUID getProductId() {
        return productId;
    }

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

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID productId;
        private String code;
        private String name;
        private String description;
        private Long stock;
        private Packaging packaging;
        private Integer quantity;
        private Measure measure;
        private BigDecimal price;
        private Currency currency;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder stock(Long stock) {
            this.stock = stock;
            return this;
        }

        public Builder packaging(Packaging packaging) {
            this.packaging = packaging;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder measure(Measure measure) {
            this.measure = measure;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(ZonedDateTime modified) {
            this.modified = modified;
            return this;
        }

        public ProductDto build() {
            final var target = new ProductDto();
            target.productId = this.productId;
            target.code = this.code;
            target.name = this.name;
            target.description = this.description;
            target.stock = this.stock;
            target.packaging = this.packaging;
            target.quantity = this.quantity;
            target.measure = this.measure;
            target.price = this.price;
            target.currency = this.currency;
            target.modified = this.modified;
            target.created = this.created;
            return target;
        }
    }
}

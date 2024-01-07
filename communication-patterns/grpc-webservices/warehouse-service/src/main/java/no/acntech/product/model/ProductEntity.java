package no.acntech.product.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "PRODUCTS")
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UUID productId;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Integer stock;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Packaging packaging;
    @Column(nullable = false)
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Measure measure;
    @Column(nullable = false)
    private Double price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    @Column
    private ZonedDateTime modified;

    public Long getId() {
        return id;
    }

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

    public Integer getStock() {
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

    public Double getPrice() {
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

    @PrePersist
    private void prePersist() {
        productId = UUID.randomUUID();
        created = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modified = ZonedDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String code;
        private String name;
        private String description;
        private Integer stock;
        private Packaging packaging;
        private Integer quantity;
        private Measure measure;
        private Double price;
        private Currency currency;

        private Builder() {
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

        public Builder stock(Integer stock) {
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

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public ProductEntity build() {
            final var target = new ProductEntity();
            target.code = this.code;
            target.name = this.name;
            target.description = this.description;
            target.stock = this.stock;
            target.packaging = this.packaging;
            target.quantity = this.quantity;
            target.measure = this.measure;
            target.price = this.price;
            target.currency = this.currency;
            return target;
        }
    }
}

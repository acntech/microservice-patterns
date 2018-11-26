package no.acntech.product.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "PRODUCTS")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private UUID productId;
    @NotBlank
    private String name;
    private String description;
    private ZonedDateTime created;

    public Long getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    @PrePersist
    public void prePersist() {
        productId = UUID.randomUUID();
        created = ZonedDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private String description;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.description = this.description;
            product.name = this.name;
            return product;
        }
    }
}

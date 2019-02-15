package no.acntech.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.acntech.product.model.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Table(name = "INVENTORY")
@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    @NotNull
    private Long quantity;
    private ZonedDateTime created;
    private ZonedDateTime modified;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    @PrePersist
    public void prePersist() {
        created = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modified = ZonedDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Product product;
        private Long quantity;

        private Builder() {
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Inventory build() {
            Inventory inventory = new Inventory();
            inventory.product = this.product;
            inventory.quantity = this.quantity;
            return inventory;
        }
    }
}

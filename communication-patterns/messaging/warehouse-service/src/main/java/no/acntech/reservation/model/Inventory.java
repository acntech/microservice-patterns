package no.acntech.reservation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Inventory {

    @Id
    @GeneratedValue
    private Long id;
    private String productId;
    private int quantity;

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean reserve(Integer quantity) {
        if (this.quantity < quantity) {
            return false;
        } else {
            this.quantity = this.quantity - quantity;
            return true;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String productId;
        private int quantity;

        private Builder() {
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Inventory build() {
            Inventory inventory = new Inventory();
            inventory.productId = this.productId;
            inventory.quantity = this.quantity;
            return inventory;
        }
    }
}

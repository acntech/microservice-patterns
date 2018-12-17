package no.acntech.warehouse.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory")
public class Inventory {

    @Id
    private String id;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private int quantity;
    private String name;

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public boolean reserve(Integer quantity) {
        if (this.quantity < quantity) {
            return false;
        } else {
            this.quantity = this.quantity - quantity;
            return true;
        }
    }

    public static builder builer() {
        return new builder();
    }

    public static final class builder {

        private int quantity;
        private String name;

        private builder() {
        }

        public builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public builder name(String name) {
            this.name = name;
            return this;
        }

        public Inventory build() {
            Inventory inventory = new Inventory();
            inventory.quantity = this.quantity;
            inventory.name = this.name;
            return inventory;
        }
    }
}

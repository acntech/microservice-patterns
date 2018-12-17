package no.acntech.warehouse.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewInventory {

    @NotNull
    @Min(1)
    private int quantity;
    @NotEmpty
    private String name;

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public static builder builder() {
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

        public NewInventory build() {
            NewInventory newInventory = new NewInventory();
            newInventory.name = this.name;
            newInventory.quantity = this.quantity;
            return newInventory;
        }
    }
}

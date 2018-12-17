package no.acntech.order.entity;

public class Orderline {

    private String productId;
    private Integer quantity;

    private Orderline() {
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void increaseQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String productId;
        private Integer quantity;

        private Builder() {
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Orderline build() {
            Orderline orderline = new Orderline();
            orderline.productId = this.productId;
            orderline.quantity = this.quantity;
            return orderline;
        }
    }
}
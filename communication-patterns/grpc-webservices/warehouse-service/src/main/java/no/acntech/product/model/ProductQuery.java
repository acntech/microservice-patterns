package no.acntech.product.model;

public class ProductQuery {

    private String name;

    private ProductQuery() {
    }

    public String getName() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public ProductQuery build() {
            final var target = new ProductQuery();
            target.name = this.name;
            return target;
        }
    }
}

package no.acntech.product.model;

public class ProductQuery {

    private String name;

    public ProductQuery() {
    }

    public ProductQuery(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package no.acntech.order.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Orderline {

    @Id
    @GeneratedValue
    private Long id;
    private String productId;
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

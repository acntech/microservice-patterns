package no.acntech.inventory.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.LastModifiedDate;

import no.acntech.product.model.Product;

@Table(name = "INVENTORIES")
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
    @LastModifiedDate
    private ZonedDateTime modified;

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ZonedDateTime getModified() {
        return modified;
    }
}

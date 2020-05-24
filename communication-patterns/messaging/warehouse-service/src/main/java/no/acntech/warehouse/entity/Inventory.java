package no.acntech.warehouse.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Inventory {

    @Id
    @GeneratedValue
    private Long id;
    private String productId;
    private int quantity;

    public boolean reserve(Integer quantity) {
        if (this.quantity < quantity) {
            return false;
        } else {
            this.quantity = this.quantity - quantity;
            return true;
        }
    }
}

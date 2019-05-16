package no.acntech.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    private Orderstatus orderstatus;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Orderline> orderlines = new ArrayList<>();
    private String warehouseReservationId;
    private boolean shipped;

}

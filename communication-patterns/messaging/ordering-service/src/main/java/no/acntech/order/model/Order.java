package no.acntech.order.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    private Orderstatus orderstatus;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Orderline> orderlines = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Orderstatus getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(Orderstatus orderstatus) {
        this.orderstatus = orderstatus;
    }

    public List<Orderline> getOrderlines() {
        return orderlines;
    }

    public Map<String, Integer> orderlinesAsMap() {
        return this.orderlines.stream()
                .collect(Collectors.toMap(Orderline::getProductId, Orderline::getQuantity));
    }
}

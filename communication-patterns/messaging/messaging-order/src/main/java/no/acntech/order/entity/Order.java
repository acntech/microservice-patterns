package no.acntech.order.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    private Orderstatus orderstatus;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Orderline> orderlines = new ArrayList<>();

    public Map<String, Integer> orderlinesAsMap() {
        return this.orderlines.stream()
                .collect(Collectors.toMap(Orderline::getProductId, Orderline::getQuantity));
    }
}

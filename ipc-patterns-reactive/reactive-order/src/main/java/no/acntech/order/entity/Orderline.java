package no.acntech.order.entity;

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
public class Orderline {

    @Id
    @GeneratedValue
    private Long id;
    private String productId;
    private Integer quantity;
}

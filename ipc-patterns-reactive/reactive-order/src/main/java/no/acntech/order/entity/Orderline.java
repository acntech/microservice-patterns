package no.acntech.order.entity;

import lombok.Data;

@Data
public class Orderline {

    private String productId;
    private Integer quantity;
}

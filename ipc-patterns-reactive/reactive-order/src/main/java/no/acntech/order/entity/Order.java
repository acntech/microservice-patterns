package no.acntech.order.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    @CreatedDate
    private LocalDateTime createdDate;
    private Orderstatus orderstatus;
    private List<Orderline> orderlines = new ArrayList<>();
    private String warehouseReservationId;
    private boolean shipped;


}

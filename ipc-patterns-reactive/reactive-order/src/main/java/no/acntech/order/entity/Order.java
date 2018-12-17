package no.acntech.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private String customerId;
    private Orderstatus orderstatus;
    private List<Orderline> orderlines = new ArrayList<>();
    private String warehouseReservationId;
    private boolean shipped;

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Orderstatus getOrderstatus() {
        return orderstatus;
    }

    public List<Orderline> getOrderlines() {
        return orderlines;
    }

    public String getWarehouseReservationId() {
        return warehouseReservationId;
    }

    public boolean isShipped() {
        return shipped;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public boolean addOrderline(Orderline orderline) {
        for (Orderline o : orderlines) {
            if (o.getProductId().equals(orderline.getProductId())) {
                o.increaseQuantity(orderline.getQuantity());
                return true;
            }
        }
        return this.orderlines.add(orderline);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String customerId;

        private Builder() {
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.customerId = this.customerId;
            return order;
        }
    }
}

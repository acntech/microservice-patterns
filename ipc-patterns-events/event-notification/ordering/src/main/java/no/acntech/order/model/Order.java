package no.acntech.order.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SortNatural;

@Table(name = "ORDERS")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID customerId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @SortNatural
    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
    private List<OrderLine> orderLines = new ArrayList<>();
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public Long getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    @PrePersist
    public void prePersist() {
        orderId = UUID.randomUUID();
        status = Status.CREATED;
        created = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modified = ZonedDateTime.now();
    }

    public enum Status {
        CREATED,
        PENDING,
        COMPLETED
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID customerId;

        private Builder() {
        }

        public Builder customerId(UUID customerId) {
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

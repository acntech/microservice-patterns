package no.acntech.customer.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "CUSTOMERS")
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private UUID customerId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String address;
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    @PrePersist
    public void prePersist() {
        this.customerId = UUID.randomUUID();
        this.created = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modified = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String firstName;
        private String lastName;
        private String address;

        private Builder() {
        }

        public Builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastname(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder address(final String address) {
            this.address = address;
            return this;
        }

        public Customer build() {
            final Customer customer = new Customer();
            customer.firstName = this.firstName;
            customer.lastName = this.lastName;
            customer.address = this.address;
            return customer;
        }
    }
}

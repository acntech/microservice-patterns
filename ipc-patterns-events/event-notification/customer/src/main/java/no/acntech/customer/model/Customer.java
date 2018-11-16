package no.acntech.customer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
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
        this.created = ZonedDateTime.now();
        this.customerId = UUID.randomUUID();
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

        private UUID customerId;
        private String firstName;
        private String lastName;
        private String address;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public Builder customerId(final UUID customerId) {
            this.customerId = customerId;
            return this;
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

        public Builder created(final ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(final ZonedDateTime modified) {
            this.modified = modified;
            return this;
        }

        public Customer build() {
            final Customer customer = new Customer();
            customer.customerId = this.customerId;
            customer.firstName = this.firstName;
            customer.created = this.created;
            customer.modified = this.modified;
            customer.lastName = this.lastName;
            customer.address = this.address;
            return customer;
        }
    }
}

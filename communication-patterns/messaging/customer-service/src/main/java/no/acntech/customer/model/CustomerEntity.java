package no.acntech.customer.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "CUSTOMERS")
@Entity
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UUID customerId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    private ZonedDateTime modified;

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

    @PrePersist
    private void prePersist() {
        this.customerId = UUID.randomUUID();
        this.created = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.modified = ZonedDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("Duplicates")
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

        public Builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder address(final String address) {
            this.address = address;
            return this;
        }

        public CustomerEntity build() {
            var target = new CustomerEntity();
            target.firstName = this.firstName;
            target.lastName = this.lastName;
            target.address = this.address;
            return target;
        }
    }
}

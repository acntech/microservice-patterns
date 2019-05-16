package no.acntech.customer.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

@Valid
public class CustomerDto {

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

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(ZonedDateTime modified) {
            this.modified = modified;
            return this;
        }

        public CustomerDto build() {
            CustomerDto customerDto = new CustomerDto();
            customerDto.customerId = this.customerId;
            customerDto.lastName = this.lastName;
            customerDto.created = this.created;
            customerDto.modified = this.modified;
            customerDto.firstName = this.firstName;
            customerDto.address = this.address;
            return customerDto;
        }
    }
}

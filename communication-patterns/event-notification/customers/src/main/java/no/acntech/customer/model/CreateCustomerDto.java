package no.acntech.customer.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Valid
public class CreateCustomerDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String address;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }
}

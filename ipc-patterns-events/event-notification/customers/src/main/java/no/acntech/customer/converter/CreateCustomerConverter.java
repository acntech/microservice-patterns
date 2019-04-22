package no.acntech.customer.converter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import no.acntech.customer.model.CreateCustomer;
import no.acntech.customer.model.Customer;

@Component
public class CreateCustomerConverter implements Converter<CreateCustomer, Customer> {

    @Override
    public Customer convert(@Valid @NotNull final CreateCustomer createCustomer) {
        return Customer.builder()
                .firstName(createCustomer.getFirstName())
                .lastName(createCustomer.getLastName())
                .address(createCustomer.getAddress())
                .build();
    }
}

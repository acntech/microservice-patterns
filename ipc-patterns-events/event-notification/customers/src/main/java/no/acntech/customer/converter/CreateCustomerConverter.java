package no.acntech.customer.converter;

import no.acntech.customer.model.CreateCustomer;
import no.acntech.customer.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class CreateCustomerConverter implements Converter<CreateCustomer, Customer> {

    @Override
    public Customer convert(@Valid @NotNull final CreateCustomer createCustomer) {
        return Customer.builder()
                .firstName(createCustomer.getFirstName())
                .lastname(createCustomer.getLastName())
                .address(createCustomer.getAddress())
                .build();
    }
}

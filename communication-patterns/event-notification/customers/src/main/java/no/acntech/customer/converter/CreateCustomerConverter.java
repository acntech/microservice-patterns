package no.acntech.customer.converter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import no.acntech.customer.model.CreateCustomerDto;
import no.acntech.customer.model.Customer;

@Component
public class CreateCustomerConverter implements Converter<CreateCustomerDto, Customer> {

    @Override
    public Customer convert(@Valid @NotNull final CreateCustomerDto createCustomer) {
        return Customer.builder()
                .firstName(createCustomer.getFirstName())
                .lastName(createCustomer.getLastName())
                .address(createCustomer.getAddress())
                .build();
    }
}

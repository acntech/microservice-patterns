package no.acntech.customer.converter;

import no.acntech.customer.model.CreateCustomerDto;
import no.acntech.customer.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

package no.acntech.customer.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.customer.model.Customer;
import no.acntech.customer.model.CustomerDto;

@Component
public class CustomerDtoConverter implements Converter<Customer, CustomerDto> {

    @Override
    public CustomerDto convert(@NonNull final Customer customer) {
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .created(customer.getCreated())
                .modified(customer.getModified())
                .build();
    }
}

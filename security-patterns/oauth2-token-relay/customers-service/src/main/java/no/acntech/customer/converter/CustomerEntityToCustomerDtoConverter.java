package no.acntech.customer.converter;

import no.acntech.customer.model.CustomerDto;
import no.acntech.customer.model.CustomerEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityToCustomerDtoConverter implements Converter<CustomerEntity, CustomerDto> {

    @NonNull
    @Override
    public CustomerDto convert(@NonNull final CustomerEntity source) {
        return CustomerDto.builder()
                .customerId(source.getCustomerId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .address(source.getAddress())
                .created(source.getCreated())
                .modified(source.getModified())
                .build();
    }
}

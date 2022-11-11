package no.acntech.customer.service;

import no.acntech.customer.exception.CustomerNotFoundException;
import no.acntech.customer.model.CreateCustomerDto;
import no.acntech.customer.model.CustomerDto;
import no.acntech.customer.model.CustomerEntity;
import no.acntech.customer.model.CustomerQuery;
import no.acntech.customer.repository.CustomerRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Service
public class CustomerService {

    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final CustomerRepository customerRepository;

    public CustomerService(final ConversionService conversionService,
                           final CustomerRepository customerRepository) {
        this.conversionService = conversionService;
        this.customerRepository = customerRepository;
    }

    public CustomerDto getCustomer(@NotNull final UUID customerId) {
        return customerRepository.findByCustomerId(customerId)
                .map(this::convert)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public List<CustomerDto> findCustomers(@NotNull @Valid final CustomerQuery customerQuery) {
        final var firstName = customerQuery.getFirstName();
        final var lastName = customerQuery.getLastName();
        if (StringUtils.hasText(firstName) && StringUtils.hasText(lastName)) {
            return customerRepository.findAllByFirstNameAndLastName(firstName, lastName).stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (StringUtils.hasText(firstName)) {
            return customerRepository.findAllByFirstName(firstName).stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (StringUtils.hasText(lastName)) {
            return customerRepository.findAllByLastName(lastName).stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return customerRepository.findAll(SORT_BY_ID).stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CustomerDto createCustomer(@NotNull @Valid final CreateCustomerDto createCustomerDto) {
        final var customerEntity = conversionService.convert(createCustomerDto, CustomerEntity.class);
        Assert.notNull(customerEntity, "Failed to convert CreateCustomerDto to CustomerEntity");
        final var savedCustomerEntity = customerRepository.save(customerEntity);
        return convert(savedCustomerEntity);
    }

    private CustomerDto convert(final CustomerEntity customerEntity) {
        final var customerDto = conversionService.convert(customerEntity, CustomerDto.class);
        Assert.notNull(customerEntity, "Failed to convert CustomerEntity to CustomerDto");
        return customerDto;
    }
}

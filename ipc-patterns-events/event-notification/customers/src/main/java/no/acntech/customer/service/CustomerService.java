package no.acntech.customer.service;

import java.util.List;
import java.util.UUID;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import no.acntech.customer.exception.CustomerNotFoundException;
import no.acntech.customer.model.CreateCustomerDto;
import no.acntech.customer.model.Customer;
import no.acntech.customer.model.CustomerQuery;
import no.acntech.customer.repository.CustomerRepository;

@Service
public class CustomerService {

    private final ConversionService conversionService;
    private final CustomerRepository customerRepository;

    public CustomerService(final ConversionService conversionService,
                           final CustomerRepository customerRepository) {
        this.conversionService = conversionService;
        this.customerRepository = customerRepository;
    }

    public Customer getCustomer(final UUID customerId) {
        return customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public List<Customer> findCustomers(final CustomerQuery customerQuery) {
        String firstName = customerQuery.getFirstName();
        String lastName = customerQuery.getLastName();
        if (StringUtils.hasText(firstName) && StringUtils.hasText(lastName)) {
            return customerRepository.findAllByFirstNameAndLastName(firstName, lastName);
        } else if (StringUtils.hasText(firstName)) {
            return customerRepository.findAllByFirstName(firstName);
        } else if (StringUtils.hasText(lastName)) {
            return customerRepository.findAllByLastName(lastName);
        } else {
            return customerRepository.findAll();
        }
    }

    @Transactional
    public Customer createCustomer(final CreateCustomerDto createCustomer) {
        Customer customer = conversionService.convert(createCustomer, Customer.class);
        return customerRepository.save(customer);
    }
}

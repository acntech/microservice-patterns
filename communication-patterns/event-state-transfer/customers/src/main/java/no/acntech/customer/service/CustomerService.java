package no.acntech.customer.service;

import no.acntech.customer.exception.CustomerNotFoundException;
import no.acntech.customer.model.CreateCustomer;
import no.acntech.customer.model.Customer;
import no.acntech.customer.repository.CustomerRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    public List<Customer> findCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer createCustomer(final CreateCustomer createCustomer) {
        Customer customer = conversionService.convert(createCustomer, Customer.class);
        return customerRepository.save(customer);
    }
}

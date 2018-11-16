package no.acntech.customer.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import no.acntech.customer.model.Customer;
import no.acntech.customer.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findCustomer(final UUID customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

}

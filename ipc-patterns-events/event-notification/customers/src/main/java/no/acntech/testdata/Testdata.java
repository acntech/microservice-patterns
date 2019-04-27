package no.acntech.testdata;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import no.acntech.customer.model.Customer;
import no.acntech.customer.repository.CustomerRepository;

@Component
public class Testdata {

    private static final List<String> FIRST_NAMES = Arrays.asList("Betty", "Hayden", "Cortney", "Joey", "Britney", "Frank", "Sue", "Ellen", "Mark", "Andy", "Jill", "Steve", "Holly", "Daniel", "Eric", "Irene");
    private static final List<String> LAST_NAMES = Arrays.asList("Taylor", "Ross", "Carter", "Mitchell", "Campbell", "Jackson", "Wilson", "Morris", "Williams", "Hall", "Adams", "Baker", "Roberts", "Howard", "Fisher");
    private static final Random RANDOM = new Random();

    private final CustomerRepository customerRepository;

    public Testdata(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    private void lagTestdata() {
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            int amount = 10;
            for (int i = 0; i < amount; i++) {
                Customer customer = randomCustomer();

                List<Customer> customersForFirstNameAndLastName = customerRepository.findAllByFirstNameAndLastName(customer.getFirstName(), customer.getLastName());
                if (customersForFirstNameAndLastName.isEmpty()) {
                    customerRepository.save(customer);
                } else {
                    amount++;
                }
            }
        }
    }

    private Customer randomCustomer() {
        return Customer.builder()
                .firstName(FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size())))
                .lastName(LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size())))
                .address("Rolfsbuktveien 2")
                .build();
    }
}

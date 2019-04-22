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

    private static final List<String> FIRST_NAMES = Arrays.asList("Ørjan", "Jon", "Jørgen", "Thomas", "Kamilla", "Ismar", "Simon");
    private static final List<String> LAST_NAMES = Arrays.asList("Rust", "Johansen", "Ringen", "Fagerholm", "Slomic", "Dowerdock", "Litlehamar");
    private static final Random RANDOM = new Random();

    private final CustomerRepository customerRepository;

    public Testdata(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    private void lagTestdata() {
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            for (int i = 0; i < 10.; i++) {
                customerRepository.save(Customer.builder()
                        .firstName(FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size())))
                        .lastName(LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size())))
                        .address("Rolfsbuktveien 2")
                        .build());

            }
        }
    }
}

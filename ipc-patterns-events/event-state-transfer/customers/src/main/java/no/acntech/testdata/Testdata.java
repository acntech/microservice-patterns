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

    private static final List<String> firstNames = Arrays.asList("Ørjan", "Jon", "Jørgen", "Thomas", "Kamilla");
    private static final List<String> lastNames = Arrays.asList("Rust", "Johansen", "Ringen", "Fagerholm");
    private static final Random RANDOM = new Random();

    private final CustomerRepository customerRepository;

    public Testdata(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    private void lagTestdata() {
        for (int i = 0; i < 10.; i++) {
            customerRepository.save(Customer.builder()
                    .firstName(firstNames.get(RANDOM.nextInt(firstNames.size())))
                    .lastname(lastNames.get(RANDOM.nextInt(lastNames.size())))
                    .address("Rolfsbuktveien 2")
                    .build());

        }
    }
}

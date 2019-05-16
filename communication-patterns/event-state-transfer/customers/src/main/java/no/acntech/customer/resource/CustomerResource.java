package no.acntech.customer.resource;

import no.acntech.customer.model.CreateCustomer;
import no.acntech.customer.model.Customer;
import no.acntech.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "customers")
@RestController
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("{customerId}")
    public ResponseEntity<Customer> get(@PathVariable("customerId") final UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));

    }

    @GetMapping
    public ResponseEntity<List<Customer>> find() {
        return ResponseEntity.ok(customerService.findCustomers());
    }

    @PostMapping
    public ResponseEntity post(@Valid final CreateCustomer createCustomer) {
        Customer customer = customerService.createCustomer(createCustomer);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{customerId}")
                .buildAndExpand(customer.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}

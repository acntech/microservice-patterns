package no.acntech.customer.resource;

import javax.websocket.server.PathParam;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.customer.model.Customer;
import no.acntech.customer.service.CustomerService;

@RequestMapping(path = "customers")
@RestController
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> get(@PathVariable("customerId") final UUID customerId) {
        return ResponseEntity.of(customerService.findCustomer(customerId));

    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }
}

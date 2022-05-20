package no.acntech.customer.resource;

import no.acntech.customer.model.CreateCustomerDto;
import no.acntech.customer.model.CustomerDto;
import no.acntech.customer.model.CustomerQuery;
import no.acntech.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/customers")
@RestController
public class CustomersResource {

    private final CustomerService customerService;

    public CustomersResource(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDto> get(@PathVariable("customerId") final UUID customerId) {
        final var customerDto = customerService.getCustomer(customerId);
        return ResponseEntity.ok(customerDto);

    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> find(final CustomerQuery customerQuery) {
        final var customerDtos = customerService.findCustomers(customerQuery);
        return ResponseEntity.ok(customerDtos);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> post(@RequestBody final CreateCustomerDto createCustomerDto) {
        final var customerDto = customerService.createCustomer(createCustomerDto);
        final var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .pathSegment(customerDto.getCustomerId().toString())
                .build()
                .toUri();
        return ResponseEntity
                .created(location)
                .body(customerDto);
    }
}

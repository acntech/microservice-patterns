package no.acntech.customer.resource;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import no.acntech.customer.model.CreateCustomerDto;
import no.acntech.customer.model.Customer;
import no.acntech.customer.model.CustomerDto;
import no.acntech.customer.model.CustomerQuery;
import no.acntech.customer.service.CustomerService;

@RequestMapping(path = "customers")
@RestController
public class CustomersResource {

    private final ConversionService conversionService;
    private final CustomerService customerService;

    public CustomersResource(final ConversionService conversionService,
                             final CustomerService customerService) {
        this.conversionService = conversionService;
        this.customerService = customerService;
    }

    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDto> get(@PathVariable("customerId") final UUID customerId) {
        final Customer customer = customerService.getCustomer(customerId);
        final CustomerDto customerDto = convert(customer);
        return ResponseEntity.ok(customerDto);

    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> find(final CustomerQuery customerQuery) {
        final List<Customer> customers = customerService.findCustomers(customerQuery);
        final List<CustomerDto> customerDtos = customers.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDtos);
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody final CreateCustomerDto createCustomer) {
        final Customer customer = customerService.createCustomer(createCustomer);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{customerId}")
                .buildAndExpand(customer.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    private CustomerDto convert(final Customer customer) {
        return conversionService.convert(customer, CustomerDto.class);
    }
}

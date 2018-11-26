package no.acntech.customer.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(UUID customerId) {
        super("No customer found for customer-id " + customerId.toString());
    }
}

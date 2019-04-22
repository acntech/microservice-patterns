package no.acntech.customer.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.customer.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerId(UUID customerId);

    List<Customer> findAllByFirstName(String firstName);

    List<Customer> findAllByLastName(String lastName);

    List<Customer> findAllByFirstNameAndLastName(String firstName, String lastName);
}

package no.acntech.customer.repository;

import no.acntech.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerId(UUID customerId);

    List<Customer> findAllByFirstName(String firstName);

    List<Customer> findAllByLastName(String lastName);

    List<Customer> findAllByFirstNameAndLastName(String firstName, String lastName);
}

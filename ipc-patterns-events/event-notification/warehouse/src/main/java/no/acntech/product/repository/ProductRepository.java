package no.acntech.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.product.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}

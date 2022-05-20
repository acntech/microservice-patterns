package no.acntech.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import no.acntech.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProductId(UUID productId);

    List<ProductEntity> findAllByName(String name);
}

package no.acntech.invoice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.invoice.model.InvoiceEntity;
import no.acntech.invoice.model.InvoiceStatus;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByInvoiceId(UUID invoiceId);

    List<InvoiceEntity> findAllByOrderId(UUID orderId);

    List<InvoiceEntity> findAllByStatus(InvoiceStatus status);

    List<InvoiceEntity> findAllByOrderIdAndStatus(UUID orderId, InvoiceStatus status);
}

package no.acntech.invoice.repository;

import no.acntech.invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceId(UUID invoiceId);

    List<Invoice> findAllByOrderId(UUID orderId);

    List<Invoice> findAllByStatus(Invoice.Status status);

    List<Invoice> findAllByOrderIdAndStatus(UUID orderId, Invoice.Status status);
}

package no.acntech.invoice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.invoice.model.Invoice;
import no.acntech.invoice.model.InvoiceStatus;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceId(UUID invoiceId);

    List<Invoice> findAllByOrderId(UUID orderId);

    List<Invoice> findAllByStatus(InvoiceStatus status);

    List<Invoice> findAllByOrderIdAndStatus(UUID orderId, InvoiceStatus status);
}

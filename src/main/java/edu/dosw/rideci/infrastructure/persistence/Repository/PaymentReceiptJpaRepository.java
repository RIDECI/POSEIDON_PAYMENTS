package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.infrastructure.persistence.Entity.PaymentReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentReceiptJpaRepository
        extends JpaRepository<PaymentReceiptEntity, String> {

    Optional<PaymentReceiptEntity> findByReceiptCode(String receiptCode);

    Optional<PaymentReceiptEntity> findByTransactionId(String transactionId);
}

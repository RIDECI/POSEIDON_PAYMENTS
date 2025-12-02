package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.infrastructure.persistence.Entity.PaymentSuspensionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentSuspensionJpaRepository extends JpaRepository<PaymentSuspensionEntity, String> {

   Optional<PaymentSuspensionEntity> findByTransactionIdAndStatus(String transactionId, SuspensionStatus status);

    List<PaymentSuspensionEntity> findByTransactionId(String transactionId);
}

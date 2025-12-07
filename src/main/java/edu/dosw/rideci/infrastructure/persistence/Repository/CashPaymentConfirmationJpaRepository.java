package edu.dosw.rideci.infrastructure.persistence.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dosw.rideci.infrastructure.persistence.Entity.CashPaymentConfirmationEntity;

public interface CashPaymentConfirmationJpaRepository
        extends JpaRepository<CashPaymentConfirmationEntity, String> {

    Optional<CashPaymentConfirmationEntity> findByTransactionId(String transactionId);
}

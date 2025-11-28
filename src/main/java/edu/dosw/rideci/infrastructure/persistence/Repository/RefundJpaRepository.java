package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.infrastructure.persistence.Entity.RefundEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundJpaRepository extends JpaRepository<RefundEntity, String> {
    Optional<RefundEntity> findByTransactionId(String transactionId);

    List<RefundEntity> findByStatus(RefundStatus status);

}

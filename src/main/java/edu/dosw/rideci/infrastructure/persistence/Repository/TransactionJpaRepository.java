package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.persistence.Entity.TransactionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByStatus(TransactionStatus status);
}
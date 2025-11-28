package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.persistence.Entity.TransactionEntity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByStatus(TransactionStatus status);
    List<TransactionEntity> findByBookingId(String bookingId);
    List<TransactionEntity> findByPassengerId(String passengerId);
    
    @Query("SELECT t FROM TransactionEntity t WHERE DATE(t.createdAt) = :date")
    List<TransactionEntity> findByCreatedAtDate(@Param("date") LocalDateTime date);
    
    @Query("SELECT t FROM TransactionEntity t WHERE t.status NOT IN ('COMPLETED', 'FAILED', 'CANCELLED', 'REFUNDED')")
    List<TransactionEntity> findActivePayments();
}
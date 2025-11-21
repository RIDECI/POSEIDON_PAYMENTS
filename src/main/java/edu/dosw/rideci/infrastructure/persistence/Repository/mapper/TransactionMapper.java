package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.infrastructure.persistence.Entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) return null;
        return TransactionEntity.builder()
                .id(domain.getId())
                .bookingId(domain.getBookingId())
                .passengerId(domain.getPassengerId())
                .amount(domain.getAmount())
                .status(domain.getStatus())
                .receiptCode(domain.getReceiptCode())
                .errorMessage(domain.getErrorMessage())
                .attempts(domain.getAttempts())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;
        return Transaction.builder()
                .id(entity.getId())
                .bookingId(entity.getBookingId())
                .passengerId(entity.getPassengerId())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .receiptCode(entity.getReceiptCode())
                .errorMessage(entity.getErrorMessage())
                .attempts(entity.getAttempts())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}


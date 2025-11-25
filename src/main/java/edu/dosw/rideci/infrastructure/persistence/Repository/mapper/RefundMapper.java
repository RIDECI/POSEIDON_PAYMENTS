package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.infrastructure.persistence.Entity.RefundEntity;
import org.springframework.stereotype.Component;

@Component
public class RefundMapper {

    public RefundEntity toEntity(Refund domain) {
        if (domain == null)
            return null;

        return RefundEntity.builder()
                .id(domain.getId())
                .transactionId(domain.getTransactionId())
                .bookingId(domain.getBookingId())
                .passengerId(domain.getPassengerId())
                .refundedAmount(domain.getRefundedAmount())
                .reason(domain.getReason())
                .status(domain.getStatus())
                .requestAt(domain.getRequestAt())
                .build();
    }

    public Refund toDomain(RefundEntity entity) {
        if (entity == null)
            return null;

        return Refund.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .bookingId(entity.getBookingId())
                .passengerId(entity.getPassengerId())
                .refundedAmount(entity.getRefundedAmount())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .requestAt(entity.getRequestAt())
                .build();
    }
}

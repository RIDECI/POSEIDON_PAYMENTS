package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.infrastructure.persistence.Entity.PaymentSuspensionEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentSuspensionMapper {

    public PaymentSuspensionEntity toEntity(PaymentSuspension domain) {
        if (domain == null) return null;
        return PaymentSuspensionEntity.builder()
                .id(domain.getId())
                .transactionId(domain.getTransactionId())
                .reason(domain.getReason())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .expiresAt(domain.getExpiresAt())
                .adminId(domain.getAdminId())
                .build();
    }

    public PaymentSuspension toDomain(PaymentSuspensionEntity entity) {
        if (entity == null) return null;
        return PaymentSuspension.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .expiresAt(entity.getExpiresAt())
                .adminId(entity.getAdminId())
                .build();
    }
}

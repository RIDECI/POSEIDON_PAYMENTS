package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.infrastructure.persistence.Entity.PaymentMethodEntity;

@Component
public class PaymentMethodMapper {

    public PaymentMethodEntity toEntity(PaymentMethod domain) {
        return PaymentMethodEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .alias(domain.getAlias())
                .isDefault(domain.isDefault())
                .isActive(domain.isActive())
                .type(domain.getType())
                .build();
    }

    public PaymentMethod toDomain(PaymentMethodEntity entity) {
        return PaymentMethod.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .alias(entity.getAlias())
                .isDefault(entity.isDefault())
                .isActive(entity.isActive())
                .type(entity.getType())
                .build();
    }
}

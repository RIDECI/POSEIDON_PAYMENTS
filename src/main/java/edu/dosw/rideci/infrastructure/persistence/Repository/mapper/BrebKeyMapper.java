package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.domain.model.BrebKey;
import edu.dosw.rideci.infrastructure.persistence.Entity.BrebKeyEntity;

@Component
public class BrebKeyMapper {

    public BrebKeyEntity toEntity(BrebKey domain) {
        return BrebKeyEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .value(domain.getValue())
                .type(domain.getType())
                .isDefault(domain.isDefault())
                .build();
    }

    public BrebKey toDomain(BrebKeyEntity entity) {
        return BrebKey.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .value(entity.getValue())
                .type(entity.getType())
                .isDefault(entity.isDefault())
                .build();
    }
}

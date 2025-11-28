package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.infrastructure.persistence.Entity.AuditLogEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AuditLogMapper {

    public AuditLogEntity toEntity(AuditLog domain) {

        String id = (domain.getId() == null || domain.getId().isBlank())
                ? UUID.randomUUID().toString()
                : domain.getId();

        return AuditLogEntity.builder()
                .id(id)
                .entityType(domain.getEntityType())
                .entityId(domain.getEntityId())
                .action(domain.getAction())
                .userId(domain.getUserId())
                .userName(domain.getUserName())
                .description(domain.getDescription())
                .previousState(domain.getPreviousState())
                .newState(domain.getNewState())
                .ipAddress(domain.getIpAddress())
                .timestamp(domain.getTimestamp())
                .build();
    }

    public AuditLog toDomain(AuditLogEntity entity) {
        return AuditLog.builder()
                .id(entity.getId())
                .entityType(entity.getEntityType())
                .entityId(entity.getEntityId())
                .action(entity.getAction())
                .userId(entity.getUserId())
                .userName(entity.getUserName())
                .description(entity.getDescription())
                .previousState(entity.getPreviousState())
                .newState(entity.getNewState())
                .ipAddress(entity.getIpAddress())
                .timestamp(entity.getTimestamp())
                .build();
    }
}

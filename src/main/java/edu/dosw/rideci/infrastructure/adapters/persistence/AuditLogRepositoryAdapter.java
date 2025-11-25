package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.infrastructure.persistence.Entity.AuditLogEntity;
import edu.dosw.rideci.infrastructure.persistence.Repository.AuditLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepositoryPort {

    private final AuditLogJpaRepository repository;

    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogEntity entity = toEntity(auditLog);
        AuditLogEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<AuditLog> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<AuditLog> findAll() {
        return repository.findAllByOrderByTimestampDesc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<AuditLog> findByEntityId(String entityId) {
        return repository.findByEntityId(entityId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<AuditLog> findByEntityType(String entityType) {
        return repository.findByEntityTypeOrderByTimestampDesc(entityType)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<AuditLog> findByUserId(String userId) {
        return repository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private AuditLog toDomain(AuditLogEntity entity) {
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

    private AuditLogEntity toEntity(AuditLog domain) {
        return AuditLogEntity.builder()
                .id(domain.getId())
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
}

package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.infrastructure.persistence.Entity.AuditLogEntity;
import edu.dosw.rideci.infrastructure.persistence.Repository.AuditLogJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.AuditLogMapper;
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
        AuditLogEntity entity = AuditLogMapper.toEntity(auditLog);
        AuditLogEntity saved = repository.save(entity);
        return AuditLogMapper.toDomain(saved);
    }

    @Override
    public Optional<AuditLog> findById(String id) {
        return repository.findById(id)
                .map(AuditLogMapper::toDomain);
    }

    @Override
    public List<AuditLog> findAll() {
        return repository.findAllByOrderByTimestampDesc()
                .stream()
                .map(AuditLogMapper::toDomain)
                .toList();
    }

    @Override
    public List<AuditLog> findByEntityId(String entityId) {
        return repository.findByEntityId(entityId)
                .stream()
                .map(AuditLogMapper::toDomain)
                .toList();
    }

    @Override
    public List<AuditLog> findByEntityType(String entityType) {
        return repository.findByEntityTypeOrderByTimestampDesc(entityType)
                .stream()
                .map(AuditLogMapper::toDomain)
                .toList();
    }

    @Override
    public List<AuditLog> findByUserId(String userId) {
        return repository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(AuditLogMapper::toDomain)
                .toList();
    }
}

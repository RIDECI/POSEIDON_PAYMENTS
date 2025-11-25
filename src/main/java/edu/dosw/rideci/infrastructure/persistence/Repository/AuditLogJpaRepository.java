package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.infrastructure.persistence.Entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, String> {
    List<AuditLogEntity> findByEntityId(String entityId);
    List<AuditLogEntity> findByEntityTypeOrderByTimestampDesc(String entityType);
    List<AuditLogEntity> findByUserIdOrderByTimestampDesc(String userId);
    List<AuditLogEntity> findAllByOrderByTimestampDesc();
}

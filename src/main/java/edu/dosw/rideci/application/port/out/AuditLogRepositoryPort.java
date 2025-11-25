package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.AuditLog;

import java.util.List;
import java.util.Optional;

public interface AuditLogRepositoryPort {
    AuditLog save(AuditLog auditLog);
    
    Optional<AuditLog> findById(String id);
    
    List<AuditLog> findAll();
    
    List<AuditLog> findByEntityId(String entityId);
    
    List<AuditLog> findByEntityType(String entityType);
    
    List<AuditLog> findByUserId(String userId);
}

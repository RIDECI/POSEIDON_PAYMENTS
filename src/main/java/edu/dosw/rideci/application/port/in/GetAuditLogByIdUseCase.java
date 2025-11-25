package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.AuditLog;

import java.util.Optional;

public interface GetAuditLogByIdUseCase {
    Optional<AuditLog> getAuditLogById(String auditId);
}

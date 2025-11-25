package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.AuditLog;

public interface CreateAuditLogUseCase {
    AuditLog createAuditLog(AuditLog auditLog);
}

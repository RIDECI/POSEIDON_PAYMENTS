package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.AuditLog;

import java.util.List;

public interface GetAllAuditLogsUseCase {
    List<AuditLog> getAllAuditLogs();
}

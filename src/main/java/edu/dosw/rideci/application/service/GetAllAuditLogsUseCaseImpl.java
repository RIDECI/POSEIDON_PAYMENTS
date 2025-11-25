package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetAllAuditLogsUseCase;
import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAllAuditLogsUseCaseImpl implements GetAllAuditLogsUseCase {

    private final AuditLogRepositoryPort auditLogRepositoryPort;

    @Override
    public List<AuditLog> getAllAuditLogs() {
        log.info("Retrieving all audit logs");
        
        List<AuditLog> auditLogs = auditLogRepositoryPort.findAll();
        
        log.info("Retrieved {} audit logs", auditLogs.size());
        return auditLogs;
    }
}

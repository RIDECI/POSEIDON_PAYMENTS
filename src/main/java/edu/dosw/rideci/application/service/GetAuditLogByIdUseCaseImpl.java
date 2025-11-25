package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetAuditLogByIdUseCase;
import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAuditLogByIdUseCaseImpl implements GetAuditLogByIdUseCase {

    private final AuditLogRepositoryPort auditLogRepositoryPort;

    @Override
    public Optional<AuditLog> getAuditLogById(String auditId) {
        log.info("Retrieving audit log with id: {}", auditId);
        
        Optional<AuditLog> auditLog = auditLogRepositoryPort.findById(auditId);
        
        if (auditLog.isPresent()) {
            log.info("Audit log found: {}", auditId);
        } else {
            log.warn("Audit log not found: {}", auditId);
        }
        
        return auditLog;
    }
}

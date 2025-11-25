package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAuditLogUseCaseImpl implements CreateAuditLogUseCase {

    private final AuditLogRepositoryPort auditLogRepositoryPort;

    @Override
    public AuditLog createAuditLog(AuditLog auditLog) {
        if (auditLog.getTimestamp() == null) {
            auditLog.setTimestamp(LocalDateTime.now());
        }
        
        AuditLog saved = auditLogRepositoryPort.save(auditLog);
        log.debug("Audit log created: {} - {} on {}", 
                  saved.getAction(), saved.getEntityType(), saved.getEntityId());
        
        return saved;
    }
}

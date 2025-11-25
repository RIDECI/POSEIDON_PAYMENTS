package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.GetAllAuditLogsUseCase;
import edu.dosw.rideci.application.port.in.GetAuditLogByIdUseCase;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.infrastructure.controller.dto.Response.AuditLogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/admin/payments/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final GetAllAuditLogsUseCase getAllAuditLogsUseCase;
    private final GetAuditLogByIdUseCase getAuditLogByIdUseCase;

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllAuditLogs() {
        log.info("Received request to retrieve all audit logs");
        
        List<AuditLog> auditLogs = getAllAuditLogsUseCase.getAllAuditLogs();
        
        List<AuditLogResponse> response = auditLogs.stream()
                .map(AuditLogResponse::fromDomain)
                .toList();
        
        log.info("Returning {} audit logs", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{auditId}")
    public ResponseEntity<AuditLogResponse> getAuditLogById(@PathVariable String auditId) {
        log.info("Received request to retrieve audit log: {}", auditId);
        
        Optional<AuditLog> auditLog = getAuditLogByIdUseCase.getAuditLogById(auditId);
        
        if (auditLog.isEmpty()) {
            log.warn("Audit log not found: {}", auditId);
            return ResponseEntity.notFound().build();
        }
        
        AuditLogResponse response = AuditLogResponse.fromDomain(auditLog.get());
        return ResponseEntity.ok(response);
    }
}

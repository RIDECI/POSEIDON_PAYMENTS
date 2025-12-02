package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.application.service.CreateAuditLogUseCaseImpl;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.enums.AuditAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateAuditLogUseCaseImplTest {

    private AuditLogRepositoryPort auditLogRepositoryPort;
    private CreateAuditLogUseCaseImpl createAuditLogUseCase;

    @BeforeEach
    void setUp() {
        auditLogRepositoryPort = mock(AuditLogRepositoryPort.class);
        createAuditLogUseCase = new CreateAuditLogUseCaseImpl(auditLogRepositoryPort);
    }

    @Test
    void createAuditLog_assignsTimestampIfNull() {
        AuditLog auditLog = AuditLog.builder()
                .action(AuditAction.CREATE)
                .entityType("Transaction")
                .entityId("tx1")
                .timestamp(null)
                .build();

        when(auditLogRepositoryPort.save(any(AuditLog.class))).thenAnswer(i -> i.getArgument(0));

        AuditLog saved = createAuditLogUseCase.createAuditLog(auditLog);

        assertNotNull(saved.getTimestamp());
        assertEquals(AuditAction.CREATE, saved.getAction());
        assertEquals("Transaction", saved.getEntityType());
        assertEquals("tx1", saved.getEntityId());
        verify(auditLogRepositoryPort).save(auditLog);
    }

    @Test
    void createAuditLog_preservesTimestampIfPresent() {
        LocalDateTime now = LocalDateTime.of(2025, 11, 30, 23, 45);
        AuditLog auditLog = AuditLog.builder()
                .action(AuditAction.UPDATE)
                .entityType("Refund")
                .entityId("r1")
                .timestamp(now)
                .build();

        when(auditLogRepositoryPort.save(any(AuditLog.class))).thenAnswer(i -> i.getArgument(0));

        AuditLog saved = createAuditLogUseCase.createAuditLog(auditLog);

        assertEquals(now, saved.getTimestamp());
        assertEquals(AuditAction.UPDATE, saved.getAction());
        assertEquals("Refund", saved.getEntityType());
        assertEquals("r1", saved.getEntityId());
        verify(auditLogRepositoryPort).save(auditLog);
    }
}

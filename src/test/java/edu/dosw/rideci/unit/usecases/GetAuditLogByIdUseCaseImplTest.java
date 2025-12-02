package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.application.service.GetAuditLogByIdUseCaseImpl;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.enums.AuditAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuditLogByIdUseCaseImplTest {

    private AuditLogRepositoryPort auditLogRepositoryPort;
    private GetAuditLogByIdUseCaseImpl getAuditLogByIdUseCase;

    @BeforeEach
    void setUp() {
        auditLogRepositoryPort = mock(AuditLogRepositoryPort.class);
        getAuditLogByIdUseCase = new GetAuditLogByIdUseCaseImpl(auditLogRepositoryPort);
    }

    @Test
    void getAuditLogById_exists_returnsAuditLog() {
        AuditLog log = AuditLog.builder().id("log1").action(AuditAction.CREATE).build();
        when(auditLogRepositoryPort.findById("log1")).thenReturn(Optional.of(log));

        Optional<AuditLog> result = getAuditLogByIdUseCase.getAuditLogById("log1");

        assertTrue(result.isPresent());
        assertEquals("log1", result.get().getId());
        verify(auditLogRepositoryPort).findById("log1");
    }

    @Test
    void getAuditLogById_notExists_returnsEmpty() {
        when(auditLogRepositoryPort.findById("log2")).thenReturn(Optional.empty());

        Optional<AuditLog> result = getAuditLogByIdUseCase.getAuditLogById("log2");

        assertTrue(result.isEmpty());
        verify(auditLogRepositoryPort).findById("log2");
    }
}

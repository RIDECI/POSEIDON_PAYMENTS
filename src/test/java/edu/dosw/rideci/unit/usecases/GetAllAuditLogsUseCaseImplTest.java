package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.AuditLogRepositoryPort;
import edu.dosw.rideci.application.service.GetAllAuditLogsUseCaseImpl;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.enums.AuditAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllAuditLogsUseCaseImplTest {

    private AuditLogRepositoryPort auditLogRepositoryPort;
    private GetAllAuditLogsUseCaseImpl getAllAuditLogsUseCase;

    @BeforeEach
    void setUp() {
        auditLogRepositoryPort = mock(AuditLogRepositoryPort.class);
        getAllAuditLogsUseCase = new GetAllAuditLogsUseCaseImpl(auditLogRepositoryPort);
    }

    @Test
    void getAllAuditLogs_returnsList() {
        AuditLog log1 = AuditLog.builder().id("1").action(AuditAction.CREATE).build();
        AuditLog log2 = AuditLog.builder().id("2").action(AuditAction.UPDATE).build();
        List<AuditLog> auditLogs = Arrays.asList(log1, log2);

        when(auditLogRepositoryPort.findAll()).thenReturn(auditLogs);

        List<AuditLog> result = getAllAuditLogsUseCase.getAllAuditLogs();

        assertEquals(2, result.size());
        assertTrue(result.contains(log1));
        assertTrue(result.contains(log2));

        verify(auditLogRepositoryPort).findAll();
    }

    @Test
    void getAllAuditLogs_emptyList() {
        when(auditLogRepositoryPort.findAll()).thenReturn(List.of());

        List<AuditLog> result = getAllAuditLogsUseCase.getAllAuditLogs();

        assertTrue(result.isEmpty());
        verify(auditLogRepositoryPort).findAll();
    }
}

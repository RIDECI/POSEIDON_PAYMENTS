package edu.dosw.rideci.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.rideci.application.port.in.GetAllAuditLogsUseCase;
import edu.dosw.rideci.application.port.in.GetAuditLogByIdUseCase;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.enums.AuditAction;
import edu.dosw.rideci.infrastructure.controller.AuditLogController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuditLogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GetAllAuditLogsUseCase getAllAuditLogsUseCase;

    @Mock
    private GetAuditLogByIdUseCase getAuditLogByIdUseCase;

    @InjectMocks
    private AuditLogController controller;

    private ObjectMapper mapper;

    private AuditLog sampleLog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();

        sampleLog = AuditLog.builder()
                .id("LOG-001")
                .entityType("PAYMENT")
                .entityId("PAYU-PASS-01")
                .action(AuditAction.CREATE)
                .userId("ADMIN-01")
                .userName("System Admin")
                .description("Payment created")
                .previousState(null)
                .newState("REQUESTED")
                .ipAddress("127.0.0.1")
                .timestamp(LocalDateTime.of(2025, 11, 30, 12, 0))
                .build();
    }

    @Test
    void testGetAllAuditLogs() throws Exception {
        when(getAllAuditLogsUseCase.getAllAuditLogs()).thenReturn(List.of(sampleLog));

        mockMvc.perform(get("/api/admin/payments/audit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("LOG-001"))
                .andExpect(jsonPath("$[0].entityType").value("PAYMENT"))
                .andExpect(jsonPath("$[0].entityId").value("PAYU-PASS-01"))
                .andExpect(jsonPath("$[0].description").value("Payment created"));

        verify(getAllAuditLogsUseCase, times(1)).getAllAuditLogs();
    }

    @Test
    void testGetAuditLogById_Found() throws Exception {
        when(getAuditLogByIdUseCase.getAuditLogById("LOG-001")).thenReturn(Optional.of(sampleLog));

        mockMvc.perform(get("/api/admin/payments/audit/LOG-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("LOG-001"))
                .andExpect(jsonPath("$.entityId").value("PAYU-PASS-01"))
                .andExpect(jsonPath("$.action").value("CREATE"))
                .andExpect(jsonPath("$.userName").value("System Admin"));

        verify(getAuditLogByIdUseCase, times(1)).getAuditLogById("LOG-001");
    }

    @Test
    void testGetAuditLogById_NotFound() throws Exception {
        when(getAuditLogByIdUseCase.getAuditLogById("LOG-XYZ")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/payments/audit/LOG-XYZ"))
                .andExpect(status().isNotFound());

        verify(getAuditLogByIdUseCase, times(1)).getAuditLogById("LOG-XYZ");
    }
}

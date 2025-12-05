package edu.dosw.rideci.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.rideci.application.port.in.*;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.infrastructure.controller.AdminPaymentSuspensionController;
import edu.dosw.rideci.infrastructure.controller.dto.Request.PaymentSuspensionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminPaymentSuspensionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Mock
    private CreateSuspensionUseCase createSuspensionUseCase;
    @Mock
    private GetSuspensionUseCase getSuspensionUseCase;
    @Mock
    private UpdateSuspensionUseCase updateSuspensionUseCase;
    @Mock
    private RevokeSuspensionUseCase revokeSuspensionUseCase;
    @Mock
    private DeleteSuspensionUseCase deleteSuspensionUseCase;

    private PaymentSuspension sample;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        AdminPaymentSuspensionController controller = new AdminPaymentSuspensionController(
                createSuspensionUseCase,
                getSuspensionUseCase,
                updateSuspensionUseCase,
                revokeSuspensionUseCase,
                deleteSuspensionUseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        sample = PaymentSuspension.builder()
                .id("SPN-001")
                .transactionId("PAY-123")
                .status(SuspensionStatus.ACTIVE)
                .reason("Fraude sospechoso")
                .adminId("ADM-001")
                .expiresAt(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void testCreateSuspension() throws Exception {
        PaymentSuspensionRequest req = new PaymentSuspensionRequest();
        req.setTransactionId("PAY-123");
        req.setAdminId("ADM-001");
        req.setReason("Fraude sospechoso");
        req.setExpiresAt(LocalDateTime.now().plusDays(1));

        Mockito.when(createSuspensionUseCase.create(any())).thenReturn(sample);

        mockMvc.perform(post("/api/admin/payments/suspensions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("PAY-123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testGetSuspensionById() throws Exception {
        Mockito.when(getSuspensionUseCase.getById("SPN-001")).thenReturn(Optional.of(sample));

        mockMvc.perform(get("/api/admin/payments/suspensions/SPN-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("SPN-001"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testGetAllSuspensions() throws Exception {
        Mockito.when(getSuspensionUseCase.getAll()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/admin/payments/suspensions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("PAY-123"));
    }

    @Test
    void testUpdateSuspension() throws Exception {
        LocalDateTime newDate = LocalDateTime.now().plusDays(3);

        Mockito.when(updateSuspensionUseCase.update(eq("SPN-001"), any(), any(), any()))
                .thenReturn(sample);

        PaymentSuspensionRequest req = new PaymentSuspensionRequest();
        req.setReason("Validación extra");
        req.setExpiresAt(newDate);
        req.setAdminId("ADM-002");

        mockMvc.perform(patch("/api/admin/payments/suspensions/SPN-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void testRevokeSuspension() throws Exception {
        sample.setStatus(SuspensionStatus.REVOKED);

        Mockito.when(revokeSuspensionUseCase.revoke(eq("SPN-001"), any()))
                .thenReturn(sample);

        mockMvc.perform(patch("/api/admin/payments/suspensions/SPN-001/revoke"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REVOKED"));
    }

    @Test
    void testDeleteSuspension() throws Exception {
        Mockito.when(deleteSuspensionUseCase.deleteById("SPN-001")).thenReturn(true);

        mockMvc.perform(delete("/api/admin/payments/suspensions/SPN-001"))
                .andExpect(status().isNoContent());
    }
}

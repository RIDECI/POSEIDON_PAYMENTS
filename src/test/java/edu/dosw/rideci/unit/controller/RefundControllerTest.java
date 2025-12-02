package edu.dosw.rideci.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.application.port.in.*;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.infrastructure.controller.RefundController;
import edu.dosw.rideci.infrastructure.controller.dto.Request.RefundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RefundControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RefundPaymentUseCase refundPaymentUseCase;
    @Mock
    private CancelRefundUseCase cancelRefundUseCase;
    @Mock
    private DeleteRefundUseCase deleteRefundUseCase;
    @Mock
    private GetRefundUseCase getRefundUseCase;
    @Mock
    private GetRefundStatusUseCase getRefundStatusUseCase;

    private Refund refund;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Inicializa los @Mock

        RefundController controller = new RefundController(
                refundPaymentUseCase,
                cancelRefundUseCase,
                deleteRefundUseCase,
                getRefundUseCase,
                getRefundStatusUseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        refund = Refund.builder()
                .id("RFD-001")
                .transactionId("TX-01")
                .bookingId("BKG-01")
                .passengerId("PSG-01")
                .refundedAmount(100.0)
                .reason("Test Refund")
                .status(RefundStatus.REQUESTED)
                .requestAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testRefund() throws Exception {
        RefundRequest req = new RefundRequest();
        req.setAmount(100.0);
        req.setReason("Test");

        when(refundPaymentUseCase.refundPayment(anyString(), anyDouble(), anyString()))
                .thenReturn(refund);

        mockMvc.perform(patch("/api/payments/{id}/refund", "TX-01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("RFD-001"))
                .andExpect(jsonPath("$.status").value("REQUESTED"));
    }

    @Test
    void testCancelRefund() throws Exception {
        refund.setStatus(RefundStatus.PROCESSING);

        when(cancelRefundUseCase.cancel(anyString())).thenReturn(refund);

        mockMvc.perform(patch("/api/payments/{id}/refund/cancel", "RFD-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));
    }

    @Test
    void testDeleteRefundSuccess() throws Exception {
        when(deleteRefundUseCase.deleteById("RFD-001")).thenReturn(true);

        mockMvc.perform(delete("/api/payments/{id}/refund", "RFD-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRefund_NotFound() throws Exception {
        when(deleteRefundUseCase.deleteById(anyString())).thenReturn(false);

        mockMvc.perform(delete("/api/payments/{id}/refund", "RFD-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRefundById() throws Exception {
        when(getRefundUseCase.getById("RFD-001")).thenReturn(Optional.of(refund));

        mockMvc.perform(get("/api/payments/{id}/refund", "RFD-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("RFD-001"));
    }

    @Test
    void testGetRefundById_NotFound() throws Exception {
        when(getRefundUseCase.getById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/payments/{id}/refund", "RFD-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRefundHistory() throws Exception {
        when(getRefundUseCase.getAll()).thenReturn(List.of(refund));

        mockMvc.perform(get("/api/payments/refund"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("RFD-001"));
    }

    @Test
    void testGetByStatusSuccess() throws Exception {
        refund.setStatus(RefundStatus.COMPLETED);

        when(getRefundStatusUseCase.findByStatus(RefundStatus.COMPLETED))
                .thenReturn(List.of(refund));

        mockMvc.perform(get("/api/payments/statusR/COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("COMPLETED"));
    }

    @Test
    void testGetByStatus_EmptyList() throws Exception {
        when(getRefundStatusUseCase.findByStatus(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/payments/statusR/REJECTED"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetByStatus_InvalidStatus() throws Exception {
        mockMvc.perform(get("/api/payments/statusR/INVALID-STATE"))
                .andExpect(status().isBadRequest());
    }
}

package edu.dosw.rideci.unit.controller;

import edu.dosw.rideci.application.port.in.ApproveRefundUseCase;
import edu.dosw.rideci.application.port.in.AuthorizeRefundUseCase;
import edu.dosw.rideci.application.port.in.CompleteRefundUseCase;
import edu.dosw.rideci.application.port.in.ProcessRefundUseCase;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.infrastructure.controller.RefundWorkflowController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RefundWorkflowControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorizeRefundUseCase authorizeUseCase;

    @Mock
    private ProcessRefundUseCase processUseCase;

    @Mock
    private ApproveRefundUseCase approveUseCase;

    @Mock
    private CompleteRefundUseCase completeUseCase;

    private Refund refund;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        RefundWorkflowController controller = new RefundWorkflowController(
                authorizeUseCase,
                processUseCase,
                approveUseCase,
                completeUseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        refund = Refund.builder()
                .id("RFD-100")
                .transactionId("TX-10")
                .bookingId("BKG-10")
                .passengerId("PSG-10")
                .refundedAmount(50.0)
                .reason("Test")
                .status(RefundStatus.AUTHORIZED)
                .requestAt(LocalDateTime.now())
                .completedAt(null)
                .build();
    }

    @Test
    void testAuthorizeRefund() throws Exception {
        refund.setStatus(RefundStatus.AUTHORIZED);

        when(authorizeUseCase.authorize(anyString())).thenReturn(refund);

        mockMvc.perform(patch("/api/payments/{id}/authorizeR", "RFD-100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(refund.getId()))
                .andExpect(jsonPath("$.status").value("AUTHORIZED"));
    }

    @Test
    void testProcessRefund() throws Exception {
        refund.setStatus(RefundStatus.PROCESSING);

        when(processUseCase.process(anyString())).thenReturn(refund);

        mockMvc.perform(patch("/api/payments/{id}/processR", "RFD-100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));
    }

    @Test
    void testApproveRefund() throws Exception {
        refund.setStatus(RefundStatus.APPROVED);

        when(approveUseCase.approve(anyString())).thenReturn(refund);

        mockMvc.perform(patch("/api/payments/{id}/approveR", "RFD-100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testCompleteRefund() throws Exception {
        refund.setStatus(RefundStatus.COMPLETED);

        when(completeUseCase.complete(anyString())).thenReturn(refund);

        mockMvc.perform(patch("/api/payments/{id}/completeR", "RFD-100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}

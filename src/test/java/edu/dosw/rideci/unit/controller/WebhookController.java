package edu.dosw.rideci.unit.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.dosw.rideci.application.port.in.ProcessWebhookUseCase;
import edu.dosw.rideci.application.port.in.UpdatePaymentByWebhookUseCase;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.infrastructure.controller.WebhookController;
import edu.dosw.rideci.infrastructure.controller.dto.Request.WebhookPaymentRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Request.WebhookUpdateRequest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {

    @Mock
    ProcessWebhookUseCase processUseCase;

    @Mock
    UpdatePaymentByWebhookUseCase updateUseCase;

    @InjectMocks
    WebhookController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testHandleWebhook_success() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Transaction tx = new Transaction();
        tx.setId("TX-1");

        when(processUseCase.processWebhook(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(tx);

        WebhookPaymentRequest req = new WebhookPaymentRequest();
        req.setTransactionId("TX-1");

        mockMvc.perform(post("/api/payments/webhooks/payU")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(processUseCase, times(1))
                .processWebhook(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testHandleWebhook_error() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(processUseCase.processWebhook(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Error"));

        WebhookPaymentRequest req = new WebhookPaymentRequest();
        req.setTransactionId("TX-9");

        mockMvc.perform(post("/api/payments/webhooks/payU")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePaymentWebhook_success() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Transaction tx = new Transaction();
        tx.setId("TX-5");

        when(updateUseCase.updateByWebhook(any(), any(), any(), any(), any(), any()))
                .thenReturn(tx);

        WebhookUpdateRequest req = new WebhookUpdateRequest();
        req.setStatus("APPROVED");

        mockMvc.perform(patch("/api/payments/webhooks/payU/TX-5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(updateUseCase, times(1))
                .updateByWebhook(any(), any(), any(), any(), any(), any());
    }

    @Test
    void testUpdatePaymentWebhook_error() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(updateUseCase.updateByWebhook(any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Fail"));

        WebhookUpdateRequest req = new WebhookUpdateRequest();
        req.setStatus("FAILED");

        mockMvc.perform(patch("/api/payments/webhooks/payU/ERR-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}

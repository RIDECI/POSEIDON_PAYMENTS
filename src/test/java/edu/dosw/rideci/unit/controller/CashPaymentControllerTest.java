package edu.dosw.rideci.unit.controller;

import edu.dosw.rideci.application.port.in.CashPaymentQueryUseCase;
import edu.dosw.rideci.application.port.in.ConfirmCashPaymentUseCase;
import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import edu.dosw.rideci.infrastructure.controller.CashPaymentController;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CashPaymentController.class)
@Import(CashPaymentControllerTest.TestConfig.class)
class CashPaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfirmCashPaymentUseCase confirmCase;

    @Autowired
    private CashPaymentQueryUseCase queryCase;

    @Configuration
    static class TestConfig {

        @Bean
        ConfirmCashPaymentUseCase confirmCashPaymentUseCase() {
            return Mockito.mock(ConfirmCashPaymentUseCase.class);
        }

        @Bean
        CashPaymentQueryUseCase cashPaymentQueryUseCase() {
            return Mockito.mock(CashPaymentQueryUseCase.class);
        }

        @Bean
        CashPaymentController controller(
                ConfirmCashPaymentUseCase confirm,
                CashPaymentQueryUseCase query) {
            return new CashPaymentController(confirm, query);
        }
    }

    private CashPaymentConfirmation mockConfirmation() {
        return CashPaymentConfirmation.builder()
                .id("CONF-1")
                .transactionId("TXN-200")
                .bookingId("BKG-10")
                .driverId("DRV-22")
                .passengerId("USR-88")
                .amount(34000.0)
                .confirmed(true)
                .confirmedAt(LocalDateTime.now())
                .observations("Pago entregado correctamente")
                .build();
    }

  
    @Test
    void testConfirmCashPayment() throws Exception {

        Mockito.when(confirmCase.confirm(
                "CONF-1",
                "DRV-22",
                "Pago OK")).thenReturn(mockConfirmation());

        String body = """
                {
                    "driverId": "DRV-22",
                    "observations": "Pago OK"
                }
                """;

        mockMvc.perform(patch("/api/payments/cash/CONF-1/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CONF-1"))
                .andExpect(jsonPath("$.transactionId").value("TXN-200"))
                .andExpect(jsonPath("$.confirmed").value(true));
    }


    @Test
    void testGetById() throws Exception {

        Mockito.when(queryCase.getById("CONF-1"))
                .thenReturn(mockConfirmation());

        mockMvc.perform(get("/api/payments/cash/confirmations/CONF-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CONF-1"))
                .andExpect(jsonPath("$.bookingId").value("BKG-10"));
    }

    @Test
    void testGetByTransactionId() throws Exception {

        Mockito.when(queryCase.getByTransactionId("TXN-200"))
                .thenReturn(mockConfirmation());

        mockMvc.perform(get("/api/payments/cash/transactions/TXN-200/confirmation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TXN-200"))
                .andExpect(jsonPath("$.driverId").value("DRV-22"));
    }


    @Test
    void testGetAllConfirmations() throws Exception {

        Mockito.when(queryCase.getAll())
                .thenReturn(List.of(mockConfirmation()));

        mockMvc.perform(get("/api/payments/cash/confirmations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("CONF-1"))
                .andExpect(jsonPath("$[0].amount").value(34000.0));
    }
}

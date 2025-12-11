package edu.dosw.rideci.unit.controller;

import edu.dosw.rideci.application.port.in.GeneratePaymentReceiptUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentReceiptUseCase;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.infrastructure.controller.PaymentReceiptController;

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

@WebMvcTest(controllers = PaymentReceiptController.class)
@Import(PaymentReceiptControllerTest.TestConfig.class)
class PaymentReceiptControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private GeneratePaymentReceiptUseCase generateCase;

        @Autowired
        private GetPaymentReceiptUseCase getCase;

        @Configuration
        static class TestConfig {

                @Bean
                GeneratePaymentReceiptUseCase generatePaymentReceiptUseCase() {
                        return Mockito.mock(GeneratePaymentReceiptUseCase.class);
                }

                @Bean
                GetPaymentReceiptUseCase getPaymentReceiptUseCase() {
                        return Mockito.mock(GetPaymentReceiptUseCase.class);
                }

                @Bean
                PaymentReceiptController controller(
                                GeneratePaymentReceiptUseCase g,
                                GetPaymentReceiptUseCase r) {
                        return new PaymentReceiptController(g, r);
                }
        }

        private PaymentReceipt mockReceipt() {
                return PaymentReceipt.builder()
                                .id("RCT-100")
                                .transactionId("TXN-1")
                                .receiptCode("RCPT-ABC")
                                .passengerId("USR-200")
                                .driverId("DRV-200")
                                .bookingId("BOOK-5")
                                .amount(78000.0)
                                .paymentMethod("NEQUI")
                                .transactionMethod("CONTRA_ENTREGA")
                                .paymentDetails("Pago exitoso")
                                .issuedAt(LocalDateTime.now())
                                .downloadUrl("http://localhost/download/receipt/RCT-100")
                                .build();
        }

        @Test
        void testGenerateReceipt() throws Exception {
                Mockito.when(generateCase.generate("TXN-1", "DRV-200"))
                                .thenReturn(mockReceipt());

                String body = """
                                {
                                    "driverId": "DRV-200"
                                }
                                """;

                mockMvc.perform(patch("/api/payments/receipt/TXN-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("RCT-100"))
                                .andExpect(jsonPath("$.transactionId").value("TXN-1"))
                                .andExpect(jsonPath("$.driverId").value("DRV-200"));
        }

        @Test
        void testGetById() throws Exception {
                Mockito.when(getCase.getById("RCT-100"))
                                .thenReturn(mockReceipt());

                mockMvc.perform(get("/api/payments/receipt/RCT-100"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.receiptCode").value("RCPT-ABC"));
        }

        @Test
        void testGetByCode() throws Exception {
                Mockito.when(getCase.getByReceiptCode("RCPT-ABC"))
                                .thenReturn(mockReceipt());

                mockMvc.perform(get("/api/payments/receipt/code/RCPT-ABC"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.receiptCode").value("RCPT-ABC"));
        }

        @Test
        void testGetByTransaction() throws Exception {
                Mockito.when(getCase.getByTransactionId("TXN-1"))
                                .thenReturn(mockReceipt());

                mockMvc.perform(get("/api/payments/receipt/transaction/TXN-1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.transactionMethod").value("CONTRA_ENTREGA"));
        }

        @Test
        void testGetAll() throws Exception {
                Mockito.when(getCase.getAll())
                                .thenReturn(List.of(mockReceipt()));

                mockMvc.perform(get("/api/payments/receipt"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value("RCT-100"))
                                .andExpect(jsonPath("$[0].paymentMethod").value("NEQUI"));
        }
}

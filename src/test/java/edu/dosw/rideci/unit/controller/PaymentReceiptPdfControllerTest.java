package edu.dosw.rideci.unit.controller;

import edu.dosw.rideci.application.port.in.GenerateReceiptPdfUseCase;
import edu.dosw.rideci.infrastructure.controller.PaymentReceiptPdfController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentReceiptPdfController.class)
@Import(PaymentReceiptPdfControllerTest.TestConfig.class)
class PaymentReceiptPdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenerateReceiptPdfUseCase pdfUseCase;

    @Configuration
    static class TestConfig {

        @Bean
        public GenerateReceiptPdfUseCase pdfUseCase() {
            return Mockito.mock(GenerateReceiptPdfUseCase.class);
        }

        @Bean
        public PaymentReceiptPdfController paymentReceiptPdfController(GenerateReceiptPdfUseCase pdfUseCase) {
            return new PaymentReceiptPdfController(pdfUseCase);
        }
    }

    @Test
    void shouldReturnPdfForPaymentReceipt() throws Exception {

        String id = "PAY-123";
        byte[] pdfBytes = "FAKE_PDF".getBytes();

        Mockito.when(pdfUseCase.generatePdf(id))
                .thenReturn(pdfBytes);

        mockMvc.perform(get("/api/payments/receipt/{id}/pdf", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(header().string(
                        "Content-Disposition",
                        "attachment; filename=receipt-" + id + ".pdf"
                ))
                .andExpect(content().bytes(pdfBytes));

        Mockito.verify(pdfUseCase).generatePdf(id);
    }
}

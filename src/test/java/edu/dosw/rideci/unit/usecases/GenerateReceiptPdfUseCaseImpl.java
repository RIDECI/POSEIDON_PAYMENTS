package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentReceiptRepositoryPort;
import edu.dosw.rideci.application.service.GenerateReceiptPdfUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class GenerateReceiptPdfUseCaseImplTest {

    @Mock
    private PaymentReceiptRepositoryPort repo;

    @InjectMocks
    private GenerateReceiptPdfUseCaseImpl service;

    private PaymentReceipt sample;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sample = PaymentReceipt.builder()
                .id("R100")
                .receiptCode("RC-2024-0001")
                .transactionId("TX-999")
                .passengerId("P1")
                .driverId("D1")
                .bookingId("B1")
                .paymentMethod("CREDIT_CARD")
                .amount(29.99)
                .issuedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void generatePdf_ok() {
        when(repo.findById("R100")).thenReturn(Optional.of(sample));

        byte[] pdfBytes = service.generatePdf("R100");

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 100);
    }

    @Test
    void generatePdf_notFound() {
        when(repo.findById("X1")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> service.generatePdf("X1")
        );

        assertEquals("Receipt not found: X1", ex.getMessage());
    }
}

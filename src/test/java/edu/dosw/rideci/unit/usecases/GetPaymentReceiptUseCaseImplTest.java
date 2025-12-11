package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentReceiptRepositoryPort;
import edu.dosw.rideci.application.service.GetPaymentReceiptUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.exceptions.RideciBusinessException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentReceiptUseCaseImplTest {

    private PaymentReceiptRepositoryPort repo;
    private GetPaymentReceiptUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(PaymentReceiptRepositoryPort.class);
        useCase = new GetPaymentReceiptUseCaseImpl(repo);
    }

    private PaymentReceipt sample() {
        return PaymentReceipt.builder()
                .id("REC-1")
                .transactionId("TX-1")
                .receiptCode("RC-1")
                .passengerId("P1")
                .driverId("D1")
                .bookingId("B1")
                .amount(50.0)
                .paymentMethod("CASH")
                .issuedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnReceiptById() {
        when(repo.findById("REC-1")).thenReturn(Optional.of(sample()));

        PaymentReceipt result = useCase.getById("REC-1");

        assertNotNull(result);
        assertEquals("REC-1", result.getId());
    }

    @Test
    void shouldThrowWhenIdNotFound() {
        when(repo.findById("REC-X")).thenReturn(Optional.empty());

        assertThrows(RideciBusinessException.class,
                () -> useCase.getById("REC-X"));
    }

    @Test
    void shouldReturnReceiptByCode() {
        when(repo.findByReceiptCode("RC-1")).thenReturn(Optional.of(sample()));

        PaymentReceipt result = useCase.getByReceiptCode("RC-1");

        assertNotNull(result);
        assertEquals("RC-1", result.getReceiptCode());
    }

    @Test
    void shouldThrowWhenReceiptCodeNotFound() {
        when(repo.findByReceiptCode("NOPE")).thenReturn(Optional.empty());

        assertThrows(RideciBusinessException.class,
                () -> useCase.getByReceiptCode("NOPE"));
    }

    @Test
    void shouldReturnReceiptByTransactionId() {
        when(repo.findByTransactionId("TX-1")).thenReturn(Optional.of(sample()));

        PaymentReceipt result = useCase.getByTransactionId("TX-1");

        assertNotNull(result);
        assertEquals("TX-1", result.getTransactionId());
    }

    @Test
    void shouldThrowWhenTransactionIdNotFound() {
        when(repo.findByTransactionId("TX-X")).thenReturn(Optional.empty());

        assertThrows(RideciBusinessException.class,
                () -> useCase.getByTransactionId("TX-X"));
    }

    @Test
    void shouldReturnAllReceipts() {
        when(repo.findAll()).thenReturn(List.of(sample()));

        List<PaymentReceipt> results = useCase.getAll();

        assertEquals(1, results.size());
        assertEquals("REC-1", results.get(0).getId());
    }
}


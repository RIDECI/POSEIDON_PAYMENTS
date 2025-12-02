package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.VerifyPaymentApprovalUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerifyPaymentApprovalUseCaseImplTest {

    private PaymentRepositoryPort repository;
    private VerifyPaymentApprovalUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepositoryPort.class);
        useCase = new VerifyPaymentApprovalUseCaseImpl(repository);
    }

    @Test
    void verifyApproval_paymentFound_returnsPayment() {
        Transaction tx = Transaction.builder()
                .id("TX-1")
                .status(TransactionStatus.APPROVED)
                .build();
        when(repository.findById("TX-1")).thenReturn(Optional.of(tx));

        Optional<Transaction> result = useCase.verifyApproval("TX-1");

        assertTrue(result.isPresent());
        assertEquals("TX-1", result.get().getId());
        assertEquals(TransactionStatus.APPROVED, result.get().getStatus());
        verify(repository, times(1)).findById("TX-1");
    }

    @Test
    void verifyApproval_paymentNotFound_returnsEmpty() {
        when(repository.findById("TX-2")).thenReturn(Optional.empty());

        Optional<Transaction> result = useCase.verifyApproval("TX-2");

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById("TX-2");
    }
}


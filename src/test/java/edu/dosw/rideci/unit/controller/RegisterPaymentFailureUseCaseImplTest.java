package edu.dosw.rideci.unit.controller;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.RegisterPaymentFailureUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterPaymentFailureUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private RegisterPaymentFailureUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        useCase = new RegisterPaymentFailureUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void registerFailure_paymentNotFound_throwsException() {
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.registerFailure("tx1", "Network error", "ERR001"));

        assertEquals("Payment not found: tx1", ex.getMessage());
    }

    @Test
    void registerFailure_firstAttempt_incrementsAttempts() {
        Transaction tx = Transaction.builder()
                .id("tx1")
                .status(TransactionStatus.PROCESSING)
                .attempts(0)
                .build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = useCase.registerFailure("tx1", "Network error", "ERR001");

        assertEquals(1, result.getAttempts());
        assertTrue(result.getErrorMessage().contains("ERR001"));
        assertNotEquals(TransactionStatus.FAILED, result.getStatus());
    }

    @Test
    void registerFailure_maxAttempts_marksFailed() {
        Transaction tx = Transaction.builder()
                .id("tx1")
                .status(TransactionStatus.PROCESSING)
                .attempts(2) 
                .build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = useCase.registerFailure("tx1", "Timeout", "ERR002");

        assertEquals(3, result.getAttempts());
        assertEquals(TransactionStatus.FAILED, result.getStatus());
        assertTrue(result.getErrorMessage().contains("ERR002"));
    }

    @Test
    void registerFailure_partialAttempts_doesNotFail() {
        Transaction tx = Transaction.builder()
                .id("tx1")
                .status(TransactionStatus.PROCESSING)
                .attempts(1)
                .build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = useCase.registerFailure("tx1", "Card declined", "ERR003");

        assertEquals(2, result.getAttempts());
        assertEquals(TransactionStatus.PROCESSING, result.getStatus());
    }
}


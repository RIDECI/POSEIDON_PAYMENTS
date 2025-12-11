package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.CashPaymentConfirmationRepositoryPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.ConfirmCashPaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ConfirmCashPaymentUseCaseImplTest {

    @Mock
    private PaymentRepositoryPort paymentRepo;

    @Mock
    private CashPaymentConfirmationRepositoryPort cashRepo;

    @InjectMocks
    private ConfirmCashPaymentUseCaseImpl service;

    private Transaction tx;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        tx = Transaction.builder()
                .id("TX1")
                .bookingId("B1")
                .passengerId("P1")
                .paymentMethod(PaymentMethodType.CASH)
                .status(TransactionStatus.PENDING_CASH)
                .amount(15.75)
                .build();
    }

    @Test
    void confirm_ok() {
        when(paymentRepo.findById("TX1")).thenReturn(Optional.of(tx));
        when(cashRepo.findByTransactionId("TX1")).thenReturn(Optional.empty());

        CashPaymentConfirmation result = service.confirm("TX1", "DR1", "All good");

        assertNotNull(result);
        assertEquals("TX1", result.getTransactionId());
        assertEquals("DR1", result.getDriverId());
        assertTrue(result.isConfirmed());

        verify(cashRepo).save(any(CashPaymentConfirmation.class));
        verify(paymentRepo).save(tx);

        assertEquals(TransactionStatus.COMPLETED, tx.getStatus());
    }

    @Test
    void confirm_transactionNotFound() {
        when(paymentRepo.findById("BAD")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> service.confirm("BAD", "DR1", null)
        );

        assertEquals("Transaction not found", ex.getMessage());
    }

    @Test
    void confirm_notCash() {
        tx.setPaymentMethod(PaymentMethodType.NEQUI);
        when(paymentRepo.findById("TX1")).thenReturn(Optional.of(tx));

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> service.confirm("TX1", "DR1", null)
        );

        assertEquals("Only CASH payments can be confirmed", ex.getMessage());
    }

    @Test
    void confirm_notPendingCash() {
        tx.setStatus(TransactionStatus.COMPLETED);
        when(paymentRepo.findById("TX1")).thenReturn(Optional.of(tx));

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> service.confirm("TX1", "DR1", null)
        );

        assertEquals("Cash payment is not pending confirmation", ex.getMessage());
    }

    @Test
    void confirm_alreadyConfirmed() {
        when(paymentRepo.findById("TX1")).thenReturn(Optional.of(tx));
        when(cashRepo.findByTransactionId("TX1")).thenReturn(Optional.of(new CashPaymentConfirmation()));

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> service.confirm("TX1", "DR1", null)
        );

        assertEquals("Cash payment already confirmed", ex.getMessage());
    }
}

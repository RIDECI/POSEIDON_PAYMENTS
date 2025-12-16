package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.CancelTripPaymentsUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelTripPaymentsUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private CancelTripPaymentsUseCaseImpl cancelTripPaymentsUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        cancelTripPaymentsUseCase = new CancelTripPaymentsUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void cancelTripPayments_successful() {
        Transaction t1 = new Transaction();
        t1.setId("t1");
        t1.setStatus(TransactionStatus.PENDING);

        Transaction t2 = new Transaction();
        t2.setId("t2");
        t2.setStatus(TransactionStatus.PROCESSING);

        Transaction t3 = new Transaction();
        t3.setId("t3");
        t3.setStatus(TransactionStatus.COMPLETED); 
        List<Transaction> payments = Arrays.asList(t1, t2, t3);

        when(paymentRepositoryPort.findByBookingId("trip1")).thenReturn(payments);
        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Transaction> cancelled = cancelTripPaymentsUseCase.cancelTripPayments("trip1");

        assertEquals(2, cancelled.size());
        assertTrue(cancelled.stream().allMatch(p -> p.getStatus() == TransactionStatus.CANCELLED));
        assertTrue(cancelled.stream().allMatch(p -> "Trip was cancelled".equals(p.getErrorMessage())));
    }

    @Test
    void cancelTripPayments_noPaymentsFound() {
        when(paymentRepositoryPort.findByBookingId("trip2")).thenReturn(Collections.emptyList());

        RideciBusinessException exception = assertThrows(RideciBusinessException.class, () -> {
            cancelTripPaymentsUseCase.cancelTripPayments("trip2");
        });

        assertEquals("No payments found for trip: trip2", exception.getMessage());
    }

    @Test
    void cancelTripPayments_onlyNonCancellable() {
        Transaction t1 = new Transaction();
        t1.setId("t1");
        t1.setStatus(TransactionStatus.COMPLETED);

        Transaction t2 = new Transaction();
        t2.setId("t2");
        t2.setStatus(TransactionStatus.CANCELLED);

        List<Transaction> payments = Arrays.asList(t1, t2);

        when(paymentRepositoryPort.findByBookingId("trip3")).thenReturn(payments);

        List<Transaction> cancelled = cancelTripPaymentsUseCase.cancelTripPayments("trip3");

        assertTrue(cancelled.isEmpty());
    }
}


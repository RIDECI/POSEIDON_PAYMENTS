package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.CompleteTripPaymentsUseCaseImpl;
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

class CompleteTripPaymentsUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private CompleteTripPaymentsUseCaseImpl completeTripPaymentsUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        completeTripPaymentsUseCase = new CompleteTripPaymentsUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void completeTripPayments_successful() {
        Transaction t1 = new Transaction();
        t1.setId("t1");
        t1.setStatus(TransactionStatus.APPROVED);

        Transaction t2 = new Transaction();
        t2.setId("t2");
        t2.setStatus(TransactionStatus.PENDING);

        Transaction t3 = new Transaction();
        t3.setId("t3");
        t3.setStatus(TransactionStatus.APPROVED);

        List<Transaction> payments = Arrays.asList(t1, t2, t3);

        when(paymentRepositoryPort.findByBookingId("trip1")).thenReturn(payments);
        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Transaction> completed = completeTripPaymentsUseCase.completeTripPayments("trip1");

        assertEquals(2, completed.size());
        assertTrue(completed.stream().allMatch(p -> p.getStatus() == TransactionStatus.COMPLETED));
        verify(paymentRepositoryPort, times(2)).save(any(Transaction.class));
    }

    @Test
    void completeTripPayments_noPaymentsFound() {
        when(paymentRepositoryPort.findByBookingId("trip2")).thenReturn(Collections.emptyList());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, () -> {
            completeTripPaymentsUseCase.completeTripPayments("trip2");
        });

        assertEquals("No payments found for trip: trip2", ex.getMessage());
    }

    @Test
    void completeTripPayments_onlyNonApproved() {
        Transaction t1 = new Transaction();
        t1.setId("t1");
        t1.setStatus(TransactionStatus.PENDING);

        Transaction t2 = new Transaction();
        t2.setId("t2");
        t2.setStatus(TransactionStatus.FAILED);

        List<Transaction> payments = Arrays.asList(t1, t2);

        when(paymentRepositoryPort.findByBookingId("trip3")).thenReturn(payments);

        List<Transaction> completed = completeTripPaymentsUseCase.completeTripPayments("trip3");

        assertTrue(completed.isEmpty());
        verify(paymentRepositoryPort, never()).save(any(Transaction.class));
    }
}

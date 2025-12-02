package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.GetActivePaymentsUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetActivePaymentsUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private GetActivePaymentsUseCaseImpl getActivePaymentsUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        getActivePaymentsUseCase = new GetActivePaymentsUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void getActivePayments_returnsList() {
        Transaction tx1 = Transaction.builder().id("tx1").build();
        Transaction tx2 = Transaction.builder().id("tx2").build();
        List<Transaction> activePayments = Arrays.asList(tx1, tx2);

        when(paymentRepositoryPort.findActivePayments()).thenReturn(activePayments);

        List<Transaction> result = getActivePaymentsUseCase.getActivePayments();

        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));

        verify(paymentRepositoryPort).findActivePayments();
    }

    @Test
    void getActivePayments_emptyList() {
        when(paymentRepositoryPort.findActivePayments()).thenReturn(List.of());

        List<Transaction> result = getActivePaymentsUseCase.getActivePayments();

        assertTrue(result.isEmpty());
        verify(paymentRepositoryPort).findActivePayments();
    }
}

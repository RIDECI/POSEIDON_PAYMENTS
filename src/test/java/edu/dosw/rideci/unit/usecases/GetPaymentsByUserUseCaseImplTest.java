package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.GetPaymentsByUserUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentsByUserUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private GetPaymentsByUserUseCaseImpl getPaymentsByUserUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        getPaymentsByUserUseCase = new GetPaymentsByUserUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void getByUserId_returnsList() {
        String userId = "user123";
        Transaction tx1 = Transaction.builder().id("tx1").build();
        Transaction tx2 = Transaction.builder().id("tx2").build();
        List<Transaction> transactions = Arrays.asList(tx1, tx2);

        when(paymentRepositoryPort.findByPassengerId(userId)).thenReturn(transactions);

        List<Transaction> result = getPaymentsByUserUseCase.getByUserId(userId);

        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));
        verify(paymentRepositoryPort).findByPassengerId(userId);
    }

    @Test
    void getByUserId_emptyList() {
        String userId = "user456";

        when(paymentRepositoryPort.findByPassengerId(userId)).thenReturn(List.of());

        List<Transaction> result = getPaymentsByUserUseCase.getByUserId(userId);

        assertTrue(result.isEmpty());
        verify(paymentRepositoryPort).findByPassengerId(userId);
    }
}

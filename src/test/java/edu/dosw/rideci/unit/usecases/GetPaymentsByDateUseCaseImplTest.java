package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.GetPaymentsByDateUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentsByDateUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private GetPaymentsByDateUseCaseImpl getPaymentsByDateUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        getPaymentsByDateUseCase = new GetPaymentsByDateUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void getByDate_returnsList() {
        LocalDate date = LocalDate.of(2025, 11, 30);
        Transaction tx1 = Transaction.builder().id("tx1").build();
        Transaction tx2 = Transaction.builder().id("tx2").build();
        List<Transaction> transactions = Arrays.asList(tx1, tx2);

        when(paymentRepositoryPort.findByCreatedAtDate(date)).thenReturn(transactions);

        List<Transaction> result = getPaymentsByDateUseCase.getByDate(date);

        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));
        verify(paymentRepositoryPort).findByCreatedAtDate(date);
    }

    @Test
    void getByDate_emptyList() {
        LocalDate date = LocalDate.of(2025, 11, 30);

        when(paymentRepositoryPort.findByCreatedAtDate(date)).thenReturn(List.of());

        List<Transaction> result = getPaymentsByDateUseCase.getByDate(date);

        assertTrue(result.isEmpty());
        verify(paymentRepositoryPort).findByCreatedAtDate(date);
    }
}

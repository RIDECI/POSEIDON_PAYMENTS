package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.GetPaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private GetPaymentUseCaseImpl getPaymentUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        getPaymentUseCase = new GetPaymentUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void getById_existing_returnsTransaction() {
        Transaction tx = Transaction.builder().id("tx1").build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));

        Optional<Transaction> result = getPaymentUseCase.getById("tx1");

        assertTrue(result.isPresent());
        assertEquals("tx1", result.get().getId());
        verify(paymentRepositoryPort).findById("tx1");
    }

    @Test
    void getById_notExisting_returnsEmpty() {
        when(paymentRepositoryPort.findById("tx2")).thenReturn(Optional.empty());

        Optional<Transaction> result = getPaymentUseCase.getById("tx2");

        assertTrue(result.isEmpty());
        verify(paymentRepositoryPort).findById("tx2");
    }

    @Test
    void findAll_returnsList() {
        Transaction tx1 = Transaction.builder().id("tx1").build();
        Transaction tx2 = Transaction.builder().id("tx2").build();
        List<Transaction> transactions = Arrays.asList(tx1, tx2);

        when(paymentRepositoryPort.findAll()).thenReturn(transactions);

        List<Transaction> result = getPaymentUseCase.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));
        verify(paymentRepositoryPort).findAll();
    }

    @Test
    void findAll_emptyList() {
        when(paymentRepositoryPort.findAll()).thenReturn(List.of());

        List<Transaction> result = getPaymentUseCase.findAll();

        assertTrue(result.isEmpty());
        verify(paymentRepositoryPort).findAll();
    }
}

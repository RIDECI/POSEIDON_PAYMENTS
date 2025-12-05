package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.GetPaymentStatusUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentStatusUseCaseImplTest {

    private PaymentRepositoryPort repository;
    private GetPaymentStatusUseCaseImpl getPaymentStatusUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepositoryPort.class);
        getPaymentStatusUseCase = new GetPaymentStatusUseCaseImpl(repository);
    }

    @Test
    void findByStatus_returnsList() {
        Transaction tx1 = Transaction.builder().id("tx1").status(TransactionStatus.PENDING).build();
        Transaction tx2 = Transaction.builder().id("tx2").status(TransactionStatus.PENDING).build();
        List<Transaction> transactions = Arrays.asList(tx1, tx2);

        when(repository.findByStatus(TransactionStatus.PENDING)).thenReturn(transactions);

        List<Transaction> result = getPaymentStatusUseCase.findByStatus(TransactionStatus.PENDING);

        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));
        verify(repository).findByStatus(TransactionStatus.PENDING);
    }

    @Test
    void findByStatus_emptyList() {
        when(repository.findByStatus(TransactionStatus.COMPLETED)).thenReturn(List.of());

        List<Transaction> result = getPaymentStatusUseCase.findByStatus(TransactionStatus.COMPLETED);

        assertTrue(result.isEmpty());
        verify(repository).findByStatus(TransactionStatus.COMPLETED);
    }
}

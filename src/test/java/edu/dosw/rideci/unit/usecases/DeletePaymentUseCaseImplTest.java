package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.DeletePaymentUseCaseImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeletePaymentUseCaseImplTest {

    private PaymentRepositoryPort repository;
    private DeletePaymentUseCaseImpl deletePaymentUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepositoryPort.class);
        deletePaymentUseCase = new DeletePaymentUseCaseImpl(repository);
    }

    @Test
    void deletePayment_exists_returnsTrue() {
        when(repository.existsById("tx1")).thenReturn(true);

        boolean result = deletePaymentUseCase.deleteById("tx1");

        assertTrue(result);
        verify(repository).deleteById("tx1");
    }

    @Test
    void deletePayment_notExists_returnsFalse() {
        when(repository.existsById("tx2")).thenReturn(false);

        boolean result = deletePaymentUseCase.deleteById("tx2");

        assertFalse(result);
        verify(repository, never()).deleteById(anyString());
    }
}

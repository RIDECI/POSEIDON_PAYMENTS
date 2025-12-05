package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.DeleteRefundUseCaseImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteRefundUseCaseImplTest {

    private RefundRepositoryPort refundRepositoryPort;
    private DeleteRefundUseCaseImpl deleteRefundUseCase;

    @BeforeEach
    void setUp() {
        refundRepositoryPort = mock(RefundRepositoryPort.class);
        deleteRefundUseCase = new DeleteRefundUseCaseImpl(refundRepositoryPort);
    }

    @Test
    void deleteRefund_exists_returnsTrue() {
        when(refundRepositoryPort.existsById("r1")).thenReturn(true);

        boolean result = deleteRefundUseCase.deleteById("r1");

        assertTrue(result);
        verify(refundRepositoryPort).deleteById("r1");
    }

    @Test
    void deleteRefund_notExists_returnsFalse() {
        when(refundRepositoryPort.existsById("r2")).thenReturn(false);

        boolean result = deleteRefundUseCase.deleteById("r2");

        assertFalse(result);
        verify(refundRepositoryPort, never()).deleteById(anyString());
    }
}

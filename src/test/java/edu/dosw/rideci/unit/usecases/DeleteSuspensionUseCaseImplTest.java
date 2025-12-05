package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.application.service.DeleteSuspensionUseCaseImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteSuspensionUseCaseImplTest {

    private PaymentSuspensionRepositoryPort repository;
    private DeleteSuspensionUseCaseImpl deleteSuspensionUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentSuspensionRepositoryPort.class);
        deleteSuspensionUseCase = new DeleteSuspensionUseCaseImpl(repository);
    }

    @Test
    void deleteSuspension_exists_returnsTrue() {
        when(repository.existsById("sus1")).thenReturn(true);

        boolean result = deleteSuspensionUseCase.deleteById("sus1");

        assertTrue(result);
        verify(repository).deleteById("sus1");
    }

    @Test
    void deleteSuspension_notExists_returnsFalse() {
        when(repository.existsById("sus2")).thenReturn(false);

        boolean result = deleteSuspensionUseCase.deleteById("sus2");

        assertFalse(result);
        verify(repository, never()).deleteById(anyString());
    }
}

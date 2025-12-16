package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.application.service.DeletePaymentMethodUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeletePaymentMethodUseCaseImplTest {

    private PaymentMethodRepositoryPort repo;
    private DeletePaymentMethodUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(PaymentMethodRepositoryPort.class);
        useCase = new DeletePaymentMethodUseCaseImpl(repo);
    }

    @Test
    void shouldDeleteWhenPaymentMethodExists() {
        String id = "PM-1";

        PaymentMethod dummy = new PaymentMethod(); 
        dummy.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(dummy));
        doNothing().when(repo).deleteById(id);

        boolean result = useCase.delete(id);

        assertTrue(result);
        verify(repo).deleteById(id);
    }

    @Test
    void shouldReturnFalseWhenPaymentMethodDoesNotExist() {
        String id = "PM-1";

        when(repo.findById(id)).thenReturn(Optional.empty());

        boolean result = useCase.delete(id);

        assertFalse(result);
        verify(repo, never()).deleteById(anyString());
    }
}

package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.application.service.DeleteCreditCardUseCaseImpl;
import edu.dosw.rideci.domain.model.CreditCard; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteCreditCardUseCaseImplTest {

    private CreditCardRepositoryPort repo;
    private DeleteCreditCardUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(CreditCardRepositoryPort.class);
        useCase = new DeleteCreditCardUseCaseImpl(repo);
    }

    @Test
    void shouldDeleteWhenCreditCardExists() {
        String id = "CC-1";

        CreditCard card = new CreditCard(); 
        card.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(card));
        doNothing().when(repo).deleteById(id);

        boolean result = useCase.delete(id);

        assertTrue(result);
        verify(repo).deleteById(id);
    }

    @Test
    void shouldReturnFalseWhenCreditCardDoesNotExist() {
        String id = "CC-1";

        when(repo.findById(id)).thenReturn(Optional.empty());

        boolean result = useCase.delete(id);

        assertFalse(result);
        verify(repo, never()).deleteById(anyString());
    }
}

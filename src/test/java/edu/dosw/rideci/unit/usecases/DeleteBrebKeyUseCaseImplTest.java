package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.application.service.DeleteBrebKeyUseCaseImpl;
import edu.dosw.rideci.domain.model.BrebKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteBrebKeyUseCaseImplTest {

    private BrebKeyRepositoryPort repo;
    private DeleteBrebKeyUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(BrebKeyRepositoryPort.class);
        useCase = new DeleteBrebKeyUseCaseImpl(repo);
    }

    @Test
    void shouldDeleteWhenKeyExists() {
        String id = "KEY-1";

        // Usa un BrebKey real o un dummy
        BrebKey dummyKey = new BrebKey();
        dummyKey.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(dummyKey));
        doNothing().when(repo).deleteById(id);

        boolean result = useCase.delete(id);

        assertTrue(result);
        verify(repo).deleteById(id);
    }

    @Test
    void shouldReturnFalseWhenKeyDoesNotExist() {
        String id = "KEY-1";

        when(repo.findById(id)).thenReturn(Optional.empty());

        boolean result = useCase.delete(id);

        assertFalse(result);
        verify(repo, never()).deleteById(anyString());
    }
}

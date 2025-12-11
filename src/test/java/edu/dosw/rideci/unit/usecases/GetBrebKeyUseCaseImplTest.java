package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.application.service.GetBrebKeyUseCaseImpl;
import edu.dosw.rideci.domain.model.BrebKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GetBrebKeyUseCaseImplTest {

    private BrebKeyRepositoryPort repo;
    private GetBrebKeyUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(BrebKeyRepositoryPort.class);
        useCase = new GetBrebKeyUseCaseImpl(repo);
    }

    private BrebKey sample() {
        BrebKey key = new BrebKey();
        key.setId("BREB-1");
        key.setUserId("USER-1");
        key.setValue("KEY123");
        key.setDefault(false);
        return key;
    }

    // -----------------------------
    // findAll()
    // -----------------------------
    @Test
    void shouldReturnAllBrebKeys() {
        when(repo.findAll()).thenReturn(List.of(sample(), sample()));

        List<BrebKey> result = useCase.findAll();

        assertEquals(2, result.size());
    }

    // -----------------------------
    // findByUser()
    // -----------------------------
    @Test
    void shouldReturnKeysByUser() {
        when(repo.findByUserId("USER-1")).thenReturn(List.of(sample()));

        List<BrebKey> result = useCase.findByUser("USER-1");

        assertEquals(1, result.size());
        assertEquals("USER-1", result.get(0).getUserId());
    }

    // -----------------------------
    // findById()
    // -----------------------------
    @Test
    void shouldReturnKeyById() {
        BrebKey key = sample();
        when(repo.findById("BREB-1")).thenReturn(Optional.of(key));

        BrebKey result = useCase.findById("BREB-1");

        assertEquals("BREB-1", result.getId());
    }

    @Test
    void shouldThrowWhenKeyNotFoundById() {
        when(repo.findById("BREB-404")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> useCase.findById("BREB-404"));
    }

    // -----------------------------
    // findDefault()
    // -----------------------------
    @Test
    void shouldReturnDefaultKey() {
        BrebKey key = sample();
        key.setDefault(true);

        when(repo.findDefaultForUser("USER-1")).thenReturn(Optional.of(key));

        BrebKey result = useCase.findDefault("USER-1");

        assertTrue(result.isDefault());
    }

    @Test
    void shouldThrowWhenDefaultKeyNotFound() {
        when(repo.findDefaultForUser("USER-1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> useCase.findDefault("USER-1"));
    }
}

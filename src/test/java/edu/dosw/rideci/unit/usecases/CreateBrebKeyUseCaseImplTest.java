package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.application.service.CreateBrebKeyUseCaseImpl;
import edu.dosw.rideci.domain.model.BrebKey;
import edu.dosw.rideci.domain.model.enums.BrebKeyType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateBrebKeyUseCaseImplTest {

    private BrebKeyRepositoryPort repo;
    private CreateBrebKeyUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(BrebKeyRepositoryPort.class);
        useCase = new CreateBrebKeyUseCaseImpl(repo);
    }

    @Test
    void shouldCreateBrebKeySuccessfully() {
        // Arrange
        BrebKey key = BrebKey.builder()
                .userId("U1")
                .value("test@example.com")
                .type(BrebKeyType.EMAIL)
                .build();

        // Capturar el objeto que se guardará para verificarlo después
        when(repo.save(any(BrebKey.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BrebKey result = useCase.create(key);

        // Assert
        assertNotNull(result.getId());
        assertTrue(result.getId().startsWith("BREB-"));

        assertFalse(result.isDefault(), "new key must not be default");
        assertEquals("U1", result.getUserId());
        assertEquals("test@example.com", result.getValue());
        assertEquals(BrebKeyType.EMAIL, result.getType());

        verify(repo).save(any(BrebKey.class));
    }
}

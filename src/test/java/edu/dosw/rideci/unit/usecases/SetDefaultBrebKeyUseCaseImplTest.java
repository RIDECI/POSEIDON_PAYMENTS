package edu.dosw.rideci.unit.usecases;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.application.service.SetDefaultBrebKeyUseCaseImpl;
import edu.dosw.rideci.domain.model.BrebKey;
import edu.dosw.rideci.domain.model.enums.BrebKeyType;

@ExtendWith(MockitoExtension.class)
class SetDefaultBrebKeyUseCaseImplTest {

    @Mock
    private BrebKeyRepositoryPort repo;

    @InjectMocks
    private SetDefaultBrebKeyUseCaseImpl useCase;

    @Test
    void setDefault_ShouldSetKeyAsDefault() {
        // Arrange
        BrebKey key = BrebKey.builder()
                .id("123")
                .userId("u1")
                .value("AAA-BBB")
                .type(BrebKeyType.DOCUMENT)
                .isDefault(false)
                .build();

        when(repo.findById("123")).thenReturn(Optional.of(key));
        when(repo.save(any(BrebKey.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        BrebKey result = useCase.setDefault("123");

        // Assert
        assertTrue(result.isDefault(), "La llave debe quedar como default");

        verify(repo).findById("123");
        verify(repo).clearDefaults("u1");
        verify(repo).save(key);
    }

    @Test
    void setDefault_ShouldThrow_WhenNotFound() {
        // Arrange
        when(repo.findById("999")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> useCase.setDefault("999"));

        verify(repo).findById("999");
        verify(repo, never()).clearDefaults(any());
        verify(repo, never()).save(any());
    }
}

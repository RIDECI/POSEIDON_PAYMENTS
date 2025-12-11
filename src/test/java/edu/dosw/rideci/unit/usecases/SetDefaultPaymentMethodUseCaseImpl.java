package edu.dosw.rideci.unit.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.application.service.SetDefaultPaymentMethodUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.exceptions.RideciBusinessException;

@ExtendWith(MockitoExtension.class)
class SetDefaultPaymentMethodUseCaseImplTest {

    @Mock
    private PaymentMethodRepositoryPort repo;

    @InjectMocks
    private SetDefaultPaymentMethodUseCaseImpl useCase;


    @Test
    void setDefault_ShouldUnsetPreviousAndSetNewDefault() {
        // Arrange
        PaymentMethod previousDefault = PaymentMethod.builder()
                .id("PM-OLD")
                .userId("usr123")
                .alias("Old")
                .isDefault(true)
                .isActive(true)
                .build();

        PaymentMethod newDefault = PaymentMethod.builder()
                .id("PM-NEW")
                .userId("usr123")
                .alias("New")
                .isDefault(false)
                .isActive(true)
                .build();

        when(repo.findById("PM-NEW")).thenReturn(Optional.of(newDefault));
        when(repo.findDefaultForUser("usr123")).thenReturn(Optional.of(previousDefault));
        when(repo.save(any(PaymentMethod.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PaymentMethod result = useCase.setDefault("PM-NEW");

        // Assert
        assertTrue(result.isDefault(), "El método debe quedar como predeterminado");
        assertFalse(previousDefault.isDefault(), "El método anterior debe quedar NO predeterminado");

        verify(repo).findById("PM-NEW");
        verify(repo).findDefaultForUser("usr123");
        verify(repo, times(2)).save(any(PaymentMethod.class)); // viejo y nuevo
    }


    @Test
    void setDefault_ShouldSetAsDefault_WhenThereIsNoPreviousDefault() {
        // Arrange
        PaymentMethod method = PaymentMethod.builder()
                .id("PM-123")
                .userId("usr999")
                .alias("Tarjeta X")
                .isDefault(false)
                .build();

        when(repo.findById("PM-123")).thenReturn(Optional.of(method));
        when(repo.findDefaultForUser("usr999")).thenReturn(Optional.empty());
        when(repo.save(any(PaymentMethod.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PaymentMethod result = useCase.setDefault("PM-123");

        // Assert
        assertTrue(result.isDefault());

        verify(repo).findById("PM-123");
        verify(repo).findDefaultForUser("usr999");
        verify(repo).save(method);
    }


    @Test
    void setDefault_ShouldThrow_WhenPaymentMethodNotFound() {
        when(repo.findById("BAD-ID")).thenReturn(Optional.empty());

        assertThrows(RideciBusinessException.class,
                () -> useCase.setDefault("BAD-ID"));

        verify(repo).findById("BAD-ID");
        verify(repo, never()).findDefaultForUser(any());
        verify(repo, never()).save(any());
    }
}

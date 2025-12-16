package edu.dosw.rideci.unit.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.application.service.SetDefaultCreditCardUseCaseImpl;
import edu.dosw.rideci.domain.model.CreditCard;

@ExtendWith(MockitoExtension.class)
class SetDefaultCreditCardUseCaseImplTest {

    @Mock
    private CreditCardRepositoryPort repo;

    @InjectMocks
    private SetDefaultCreditCardUseCaseImpl useCase;

    @Test
    void setDefault_ShouldSetCardAsDefault() {
        CreditCard card = CreditCard.builder()
                .id("CC-1")
                .userId("user123")
                .cardHolder("John Doe")
                .cardNumber("1234")
                .expiration("12/29")
                .cvv("321")
                .alias("Mi tarjeta")
                .isDefault(false)
                .isActive(true)
                .build();

        when(repo.findById("CC-1")).thenReturn(Optional.of(card));
        when(repo.save(any(CreditCard.class))).thenAnswer(inv -> inv.getArgument(0));

        CreditCard result = useCase.setDefault("CC-1");

        assertTrue(result.isDefault(), "La tarjeta debe quedar como predeterminada");

        verify(repo).findById("CC-1");
        verify(repo).clearDefaults("user123");
        verify(repo).save(card);
    }

    @Test
    void setDefault_ShouldThrow_WhenCardNotFound() {
        when(repo.findById("XXX")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.setDefault("XXX"));

        verify(repo).findById("XXX");
        verify(repo, never()).clearDefaults(any());
        verify(repo, never()).save(any());
    }
}

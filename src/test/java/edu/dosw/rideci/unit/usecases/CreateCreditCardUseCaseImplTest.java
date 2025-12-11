package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.application.service.CreateCreditCardUseCaseImpl;
import edu.dosw.rideci.domain.model.CreditCard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateCreditCardUseCaseImplTest {

    private CreditCardRepositoryPort repo;
    private CreateCreditCardUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(CreditCardRepositoryPort.class);
        useCase = new CreateCreditCardUseCaseImpl(repo);
    }

    private CreditCard sample() {
        CreditCard c = new CreditCard();
        c.setUserId("U1");
        c.setAlias("Card alias");
        return c;
    }

    @Test
    void shouldCreateCreditCardAndGenerateId() {
        CreditCard input = sample();

        when(repo.findByUserId("U1")).thenReturn(List.of(new CreditCard()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        CreditCard result = useCase.create(input);

        assertNotNull(result.getId());
        assertTrue(result.getId().startsWith("CC-"));
        assertTrue(result.isActive());
    }

    @Test
    void shouldSetDefaultFalseWhenUserHasNoCards() {
        CreditCard input = sample();

        when(repo.findByUserId("U1")).thenReturn(List.of());
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        CreditCard result = useCase.create(input);

        assertFalse(result.isDefault());
    }

    @Test
    void shouldNotModifyDefaultWhenUserAlreadyHasCards() {
        CreditCard input = sample();
        input.setDefault(true);  // if user already has cards, should remain

        when(repo.findByUserId("U1")).thenReturn(List.of(new CreditCard()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        CreditCard result = useCase.create(input);

        assertTrue(result.isDefault());
    }

    @Test
    void shouldCallRepositorySave() {
        CreditCard input = sample();

        when(repo.findByUserId("U1")).thenReturn(List.of());
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        useCase.create(input);

        verify(repo).save(any(CreditCard.class));
    }
}

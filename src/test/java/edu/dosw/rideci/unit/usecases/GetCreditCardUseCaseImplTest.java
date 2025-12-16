package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.application.service.GetCreditCardUseCaseImpl;
import edu.dosw.rideci.domain.model.CreditCard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GetCreditCardUseCaseImplTest {

    private CreditCardRepositoryPort repo;
    private GetCreditCardUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(CreditCardRepositoryPort.class);
        useCase = new GetCreditCardUseCaseImpl(repo);
    }

    private CreditCard sample() {
        CreditCard card = new CreditCard();
        card.setId("CC-1");
        card.setUserId("USER-1");
        card.setCardHolder("Test User");
        card.setCardNumber("4111111111111111");
        card.setExpiration("12/30");
        card.setCvv("123");
        card.setAlias("Main card");
        card.setDefault(false);
        card.setActive(true);
        return card;
    }


    @Test
    void shouldReturnAllCreditCards() {
        List<CreditCard> mockList = List.of(sample(), sample());

        when(repo.findAll()).thenReturn(mockList);

        List<CreditCard> result = useCase.findAll();

        assertEquals(2, result.size());
    }


    @Test
    void shouldReturnCardsForUser() {
        List<CreditCard> mockList = List.of(sample());

        when(repo.findByUserId("USER-1")).thenReturn(mockList);

        List<CreditCard> result = useCase.findByUser("USER-1");

        assertEquals(1, result.size());
        assertEquals("USER-1", result.get(0).getUserId());
    }


    @Test
    void shouldReturnCardById() {
        CreditCard card = sample();

        when(repo.findById("CC-1")).thenReturn(Optional.of(card));

        CreditCard result = useCase.findById("CC-1");

        assertNotNull(result);
        assertEquals("CC-1", result.getId());
    }

    @Test
    void shouldThrowWhenCardNotFoundById() {
        when(repo.findById("CC-404")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> useCase.findById("CC-404"));
    }

    @Test
    void shouldReturnDefaultCard() {
        CreditCard card = sample();
        card.setDefault(true);

        when(repo.findDefaultForUser("USER-1")).thenReturn(Optional.of(card));

        CreditCard result = useCase.findDefault("USER-1");

        assertTrue(result.isDefault());
    }

    @Test
    void shouldThrowWhenDefaultCardNotFound() {
        when(repo.findDefaultForUser("USER-1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> useCase.findDefault("USER-1"));
    }
}

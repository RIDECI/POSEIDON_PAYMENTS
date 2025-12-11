package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.CashPaymentConfirmationRepositoryPort;
import edu.dosw.rideci.application.service.CashPaymentQueryUseCaseImpl;
import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import edu.dosw.rideci.exceptions.RideciBusinessException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CashPaymentQueryUseCaseImplTest {

    private CashPaymentConfirmationRepositoryPort repo;
    private CashPaymentQueryUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(CashPaymentConfirmationRepositoryPort.class);
        useCase = new CashPaymentQueryUseCaseImpl(repo);
    }

    private CashPaymentConfirmation sample() {
        CashPaymentConfirmation c = new CashPaymentConfirmation();
        c.setId("CASH-1");
        c.setTransactionId("TX-1");
        c.setAmount(5000.0);
        return c;
    }

    // ---------------------------
    // getById()
    // ---------------------------

    @Test
    void shouldReturnCashConfirmationById() {
        CashPaymentConfirmation mockData = sample();

        when(repo.findById("CASH-1")).thenReturn(Optional.of(mockData));

        CashPaymentConfirmation result = useCase.getById("CASH-1");

        assertNotNull(result);
        assertEquals("CASH-1", result.getId());
    }

    @Test
    void shouldThrowWhenCashConfirmationNotFoundById() {
        when(repo.findById("CASH-404")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> useCase.getById("CASH-404")
        );

        assertEquals("Cash confirmation not found: CASH-404", ex.getMessage());
    }

    // ---------------------------
    // getByTransactionId()
    // ---------------------------

    @Test
    void shouldReturnCashConfirmationByTransactionId() {
        CashPaymentConfirmation mockData = sample();

        when(repo.findByTransactionId("TX-1")).thenReturn(Optional.of(mockData));

        CashPaymentConfirmation result = useCase.getByTransactionId("TX-1");

        assertNotNull(result);
        assertEquals("TX-1", result.getTransactionId());
    }

    @Test
    void shouldThrowWhenNotFoundByTransactionId() {
        when(repo.findByTransactionId("TX-404")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> useCase.getByTransactionId("TX-404")
        );

        assertEquals("No confirmation found for transaction: TX-404", ex.getMessage());
    }

    // ---------------------------
    // getAll()
    // ---------------------------

    @Test
    void shouldReturnAllCashConfirmations() {
        List<CashPaymentConfirmation> list = List.of(sample(), sample());

        when(repo.findAll()).thenReturn(list);

        List<CashPaymentConfirmation> result = useCase.getAll();

        assertEquals(2, result.size());
    }
}

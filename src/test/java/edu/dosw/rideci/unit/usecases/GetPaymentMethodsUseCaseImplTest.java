package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.application.service.GetPaymentMethodsUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GetPaymentMethodsUseCaseImplTest {

    private PaymentMethodRepositoryPort repo;
    private GetPaymentMethodsUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(PaymentMethodRepositoryPort.class);
        useCase = new GetPaymentMethodsUseCaseImpl(repo);
    }

    private PaymentMethod sample() {
        return PaymentMethod.builder()
                .id("PM-1")
                .userId("U1")
                .alias("Tarjeta principal")
                .isDefault(false)
                .isActive(true)
                .type(PaymentMethodType.CREDIT_CARD_PAYU)
                .build();
    }

    @Test
    void shouldReturnMethodsByUserId() {
        when(repo.findByUserId("U1")).thenReturn(List.of(sample()));

        List<PaymentMethod> results = useCase.getByUserId("U1");

        assertEquals(1, results.size());
        assertEquals("U1", results.get(0).getUserId());
    }

    @Test
    void shouldReturnMethodById() {
        when(repo.findById("PM-1")).thenReturn(Optional.of(sample()));

        PaymentMethod result = useCase.getById("PM-1");

        assertNotNull(result);
        assertEquals("PM-1", result.getId());
    }

    @Test
    void shouldThrowWhenPaymentMethodNotFound() {
        when(repo.findById("PM-X")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> useCase.getById("PM-X"));
    }

    @Test
    void shouldReturnAllPaymentMethods() {
        when(repo.findAll()).thenReturn(List.of(sample()));

        List<PaymentMethod> results = useCase.findAll();

        assertEquals(1, results.size());
        assertEquals("PM-1", results.get(0).getId());
    }
}

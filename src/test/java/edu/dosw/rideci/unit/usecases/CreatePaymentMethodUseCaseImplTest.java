package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.application.service.CreatePaymentMethodUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentMethod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreatePaymentMethodUseCaseImplTest {

    private PaymentMethodRepositoryPort repo;
    private CreatePaymentMethodUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        repo = mock(PaymentMethodRepositoryPort.class);
        useCase = new CreatePaymentMethodUseCaseImpl(repo);
    }

    private PaymentMethod sample() {
        PaymentMethod pm = new PaymentMethod();
        pm.setUserId("U100");
        pm.setAlias("Test Method");
        return pm;
    }

    @Test
    void shouldCreatePaymentMethodAndGenerateId() {
        PaymentMethod input = sample();

        when(repo.findByUserId("U100")).thenReturn(List.of(new PaymentMethod()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        PaymentMethod result = useCase.create(input);

        assertNotNull(result.getId());
        assertTrue(result.getId().startsWith("PM-"));
        assertTrue(result.isActive());
    }

    @Test
    void shouldSetDefaultFalseWhenUserHasNoMethods() {
        PaymentMethod input = sample();

        when(repo.findByUserId("U100")).thenReturn(List.of());
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        PaymentMethod result = useCase.create(input);

        assertFalse(result.isDefault());
    }

    @Test
    void shouldNotOverrideDefaultIfUserAlreadyHasMethods() {
        PaymentMethod input = sample();
        input.setDefault(true); // user forces default for example

        when(repo.findByUserId("U100")).thenReturn(List.of(new PaymentMethod()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        PaymentMethod result = useCase.create(input);

        assertTrue(result.isDefault(), "Default should not be modified if user has methods");
    }

    @Test
    void shouldCallSaveOnRepository() {
        PaymentMethod input = sample();

        when(repo.findByUserId("U100")).thenReturn(List.of());
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        useCase.create(input);

        verify(repo).save(any(PaymentMethod.class));
    }
}

package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.application.service.GetSuspensionUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetSuspensionUseCaseImplTest {

    private PaymentSuspensionRepositoryPort repository;
    private GetSuspensionUseCaseImpl getSuspensionUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentSuspensionRepositoryPort.class);
        getSuspensionUseCase = new GetSuspensionUseCaseImpl(repository);
    }

    @Test
    void getById_existing_returnsSuspension() {
        PaymentSuspension suspension = PaymentSuspension.builder().id("s1").build();
        when(repository.findById("s1")).thenReturn(Optional.of(suspension));

        Optional<PaymentSuspension> result = getSuspensionUseCase.getById("s1");

        assertTrue(result.isPresent());
        assertEquals("s1", result.get().getId());
        verify(repository).findById("s1");
    }

    @Test
    void getById_notExisting_returnsEmpty() {
        when(repository.findById("s2")).thenReturn(Optional.empty());

        Optional<PaymentSuspension> result = getSuspensionUseCase.getById("s2");

        assertTrue(result.isEmpty());
        verify(repository).findById("s2");
    }

    @Test
    void getAll_returnsList() {
        PaymentSuspension s1 = PaymentSuspension.builder().id("s1").build();
        PaymentSuspension s2 = PaymentSuspension.builder().id("s2").build();
        List<PaymentSuspension> suspensions = Arrays.asList(s1, s2);

        when(repository.findAll()).thenReturn(suspensions);

        List<PaymentSuspension> result = getSuspensionUseCase.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(s1));
        assertTrue(result.contains(s2));
        verify(repository).findAll();
    }

    @Test
    void getAll_emptyList() {
        when(repository.findAll()).thenReturn(List.of());

        List<PaymentSuspension> result = getSuspensionUseCase.getAll();

        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }
}

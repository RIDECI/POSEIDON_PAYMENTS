package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.GetRefundUseCaseImpl;
import edu.dosw.rideci.domain.model.Refund;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRefundUseCaseImplTest {

    private RefundRepositoryPort refundRepositoryPort;
    private GetRefundUseCaseImpl getRefundUseCase;

    @BeforeEach
    void setUp() {
        refundRepositoryPort = mock(RefundRepositoryPort.class);
        getRefundUseCase = new GetRefundUseCaseImpl(refundRepositoryPort);
    }

    @Test
    void getById_existing_returnsRefund() {
        Refund refund = Refund.builder().id("r1").build();
        when(refundRepositoryPort.findById("r1")).thenReturn(Optional.of(refund));

        Optional<Refund> result = getRefundUseCase.getById("r1");

        assertTrue(result.isPresent());
        assertEquals("r1", result.get().getId());
        verify(refundRepositoryPort).findById("r1");
    }

    @Test
    void getById_notExisting_returnsEmpty() {
        when(refundRepositoryPort.findById("r2")).thenReturn(Optional.empty());

        Optional<Refund> result = getRefundUseCase.getById("r2");

        assertTrue(result.isEmpty());
        verify(refundRepositoryPort).findById("r2");
    }

    @Test
    void getAll_returnsList() {
        Refund r1 = Refund.builder().id("r1").build();
        Refund r2 = Refund.builder().id("r2").build();
        List<Refund> refunds = Arrays.asList(r1, r2);

        when(refundRepositoryPort.findAll()).thenReturn(refunds);

        List<Refund> result = getRefundUseCase.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
        verify(refundRepositoryPort).findAll();
    }

    @Test
    void getAll_emptyList() {
        when(refundRepositoryPort.findAll()).thenReturn(List.of());

        List<Refund> result = getRefundUseCase.getAll();

        assertTrue(result.isEmpty());
        verify(refundRepositoryPort).findAll();
    }
}


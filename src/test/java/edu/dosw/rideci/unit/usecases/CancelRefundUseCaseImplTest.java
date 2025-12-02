package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.CancelRefundUseCaseImpl;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelRefundUseCaseImplTest {

    private RefundRepositoryPort refundRepositoryPort;
    private CancelRefundUseCaseImpl cancelRefundUseCase;

    @BeforeEach
    void setUp() {
        refundRepositoryPort = mock(RefundRepositoryPort.class);
        cancelRefundUseCase = new CancelRefundUseCaseImpl(refundRepositoryPort);
    }

    @Test
    void cancelRefund_successful() {
        Refund refund = Refund.builder()
                .id("r1")
                .status(RefundStatus.REQUESTED)
                .build();

        when(refundRepositoryPort.findById("r1")).thenReturn(Optional.of(refund));
        when(refundRepositoryPort.save(any(Refund.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Refund result = cancelRefundUseCase.cancel("r1");

        assertEquals(RefundStatus.CANCELLED, result.getStatus());
        verify(refundRepositoryPort).save(refund);
    }

    @Test
    void cancelRefund_notFound() {
        when(refundRepositoryPort.findById("r2")).thenReturn(Optional.empty());

        RideciBusinessException exception = assertThrows(RideciBusinessException.class, () -> {
            cancelRefundUseCase.cancel("r2");
        });

        assertEquals("Refund not found: r2", exception.getMessage());
    }

    @Test
    void cancelRefund_alreadyCompleted() {
        Refund refund = Refund.builder()
                .id("r3")
                .status(RefundStatus.COMPLETED)
                .build();

        when(refundRepositoryPort.findById("r3")).thenReturn(Optional.of(refund));

        RideciBusinessException exception = assertThrows(RideciBusinessException.class, () -> {
            cancelRefundUseCase.cancel("r3");
        });

        assertEquals("Cannot cancel a completed refund", exception.getMessage());
    }

    @Test
    void cancelRefund_alreadyCancelled() {
        Refund refund = Refund.builder()
                .id("r4")
                .status(RefundStatus.CANCELLED)
                .build();

        when(refundRepositoryPort.findById("r4")).thenReturn(Optional.of(refund));

        RideciBusinessException exception = assertThrows(RideciBusinessException.class, () -> {
            cancelRefundUseCase.cancel("r4");
        });

        assertEquals("Refund is already cancelled", exception.getMessage());
    }
}

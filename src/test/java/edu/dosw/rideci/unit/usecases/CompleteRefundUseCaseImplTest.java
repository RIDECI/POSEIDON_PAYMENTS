package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.CompleteRefundUseCaseImpl;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompleteRefundUseCaseImplTest {

    private RefundRepositoryPort refundRepositoryPort;
    private CompleteRefundUseCaseImpl completeRefundUseCase;

    @BeforeEach
    void setUp() {
        refundRepositoryPort = mock(RefundRepositoryPort.class);
        completeRefundUseCase = new CompleteRefundUseCaseImpl(refundRepositoryPort);
    }

    @Test
    void completeRefund_successful() {
        Refund refund = Refund.builder()
                .id("r1")
                .status(RefundStatus.APPROVED)
                .build();

        when(refundRepositoryPort.findById("r1")).thenReturn(Optional.of(refund));
        when(refundRepositoryPort.save(any(Refund.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Refund completed = completeRefundUseCase.complete("r1");

        assertEquals(RefundStatus.COMPLETED, completed.getStatus());
        assertNotNull(completed.getCompletedAt());
        verify(refundRepositoryPort).save(refund);
    }

    @Test
    void completeRefund_notFound() {
        when(refundRepositoryPort.findById("r2")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, () -> {
            completeRefundUseCase.complete("r2");
        });

        assertEquals("Refund not found: r2", ex.getMessage());
    }

    @Test
    void completeRefund_notApproved() {
        Refund refund = Refund.builder()
                .id("r3")
                .status(RefundStatus.REQUESTED)
                .build();

        when(refundRepositoryPort.findById("r3")).thenReturn(Optional.of(refund));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, () -> {
            completeRefundUseCase.complete("r3");
        });

        assertEquals("Refund must be APPROVED to be completed", ex.getMessage());
    }
}

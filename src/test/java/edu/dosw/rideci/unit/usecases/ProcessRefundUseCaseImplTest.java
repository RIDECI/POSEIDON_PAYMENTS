package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.ProcessRefundUseCaseImpl;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessRefundUseCaseImplTest {

    private RefundRepositoryPort refundRepositoryPort;
    private ProcessRefundUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        refundRepositoryPort = mock(RefundRepositoryPort.class);
        useCase = new ProcessRefundUseCaseImpl(refundRepositoryPort);
    }

    @Test
    void process_refundNotFound_throwsException() {
        when(refundRepositoryPort.findById("r1")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.process("r1"));

        assertEquals("Refund not found: r1", ex.getMessage());
    }

    @Test
    void process_refundNotAuthorized_throwsException() {
        Refund refund = Refund.builder().id("r1").status(RefundStatus.REQUESTED).build();
        when(refundRepositoryPort.findById("r1")).thenReturn(Optional.of(refund));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.process("r1"));

        assertEquals("Refund must be AUTHORIZED before processing", ex.getMessage());
    }

    @Test
    void process_successful() {
        Refund refund = Refund.builder().id("r1").status(RefundStatus.AUTHORIZED).build();
        Refund savedRefund = Refund.builder().id("r1").status(RefundStatus.PROCESSING).build();

        when(refundRepositoryPort.findById("r1")).thenReturn(Optional.of(refund));
        when(refundRepositoryPort.save(any(Refund.class))).thenReturn(savedRefund);

        Refund result = useCase.process("r1");

        assertEquals(RefundStatus.PROCESSING, result.getStatus());
        verify(refundRepositoryPort).save(any(Refund.class));
    }
}

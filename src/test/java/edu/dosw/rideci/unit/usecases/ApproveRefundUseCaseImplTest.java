package edu.dosw.rideci.unit.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.ApproveRefundUseCaseImpl;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ApproveRefundUseCaseImplTest {

    @Mock
    private RefundRepositoryPort refundRepositoryPort;

    @InjectMocks
    private ApproveRefundUseCaseImpl approveRefundUseCase;

    private Refund refund;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        refund = Refund.builder()
                .id("R-100")
                .transactionId("TX-555")
                .bookingId("B-999")
                .passengerId("P-123")
                .refundedAmount(20000.0)
                .reason("Customer request")
                .status(RefundStatus.PROCESSING)
                .build();
    }

    @Test
    void shouldApproveRefundSuccessfully() {
        when(refundRepositoryPort.findById("R-100"))
                .thenReturn(Optional.of(refund));
        when(refundRepositoryPort.save(refund))
                .thenReturn(refund);

        Refund result = approveRefundUseCase.approve("R-100");

        assertEquals(RefundStatus.APPROVED, result.getStatus());
        verify(refundRepositoryPort).save(refund);
    }

    @Test
    void shouldThrowExceptionWhenRefundNotFound() {
        when(refundRepositoryPort.findById("XYZ"))
                .thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> approveRefundUseCase.approve("XYZ")
        );

        assertEquals("Refund not found: XYZ", ex.getMessage());
    }

    @Test
    void shouldFailWhenRefundIsNotProcessing() {
        refund.setStatus(RefundStatus.REJECTED);
        when(refundRepositoryPort.findById("R-100"))
                .thenReturn(Optional.of(refund));

        RideciBusinessException ex = assertThrows(
                RideciBusinessException.class,
                () -> approveRefundUseCase.approve("R-100")
        );

        assertEquals("Refund must be PROCESSING to be approved", ex.getMessage());
        verify(refundRepositoryPort, never()).save(any());
    }
}

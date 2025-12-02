package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.AuthorizeRefundUseCaseImpl;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizeRefundUseCaseImplTest {

    @Mock
    private RefundRepositoryPort refundRepositoryPort;

    @InjectMocks
    private AuthorizeRefundUseCaseImpl authorizeRefundUseCase;

    private Refund refund;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        refund = Refund.builder()
                .id("RF-123")
                .status(RefundStatus.REQUESTED)
                .build();
    }

    @Test
    void shouldAuthorizeRefundSuccessfully() {
        when(refundRepositoryPort.findById("RF-123"))
                .thenReturn(Optional.of(refund));
        when(refundRepositoryPort.save(refund))
                .thenReturn(refund);

        Refund result = authorizeRefundUseCase.authorize("RF-123");

        assertNotNull(result);
        assertEquals(RefundStatus.AUTHORIZED, result.getStatus());
        verify(refundRepositoryPort).findById("RF-123");
        verify(refundRepositoryPort).save(refund);
    }

    @Test
    void shouldThrowExceptionWhenRefundNotFound() {
        when(refundRepositoryPort.findById("RF-000"))
                .thenReturn(Optional.empty());

        RideciBusinessException exception = assertThrows(
                RideciBusinessException.class,
                () -> authorizeRefundUseCase.authorize("RF-000")
        );

        assertTrue(exception.getMessage().contains("Refund not found"));
        verify(refundRepositoryPort).findById("RF-000");
        verify(refundRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRefundIsNotRequested() {
        refund.setStatus(RefundStatus.PROCESSING);
        when(refundRepositoryPort.findById("RF-123"))
                .thenReturn(Optional.of(refund));

        RideciBusinessException exception = assertThrows(
                RideciBusinessException.class,
                () -> authorizeRefundUseCase.authorize("RF-123")
        );

        assertEquals("Only REQUESTED refunds can be authorized", exception.getMessage());
        verify(refundRepositoryPort).findById("RF-123");
        verify(refundRepositoryPort, never()).save(any());
    }
}


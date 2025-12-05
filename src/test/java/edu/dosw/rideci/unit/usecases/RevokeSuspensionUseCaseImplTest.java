package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.application.service.RevokeSuspensionUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RevokeSuspensionUseCaseImplTest {

    private PaymentSuspensionRepositoryPort repository;
    private RevokeSuspensionUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentSuspensionRepositoryPort.class);
        useCase = new RevokeSuspensionUseCaseImpl(repository);
    }

    @Test
    void revoke_suspensionNotFound_throwsException() {
        when(repository.findById("SUS-1")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.revoke("SUS-1", "admin1"));

        assertEquals("Suspension not found: SUS-1", ex.getMessage());
    }

    @Test
    void revoke_suspensionAlreadyRevoked_throwsException() {
        PaymentSuspension suspension = PaymentSuspension.builder()
                .id("SUS-2")
                .status(SuspensionStatus.REVOKED)
                .build();
        when(repository.findById("SUS-2")).thenReturn(Optional.of(suspension));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.revoke("SUS-2", "admin1"));

        assertEquals("Suspension already revoked", ex.getMessage());
    }

    @Test
    void revoke_successfulRevocation_updatesStatusAndAdmin() {
        PaymentSuspension suspension = PaymentSuspension.builder()
                .id("SUS-3")
                .status(SuspensionStatus.ACTIVE)
                .build();
        when(repository.findById("SUS-3")).thenReturn(Optional.of(suspension));
        when(repository.save(any(PaymentSuspension.class))).thenAnswer(i -> i.getArguments()[0]);

        PaymentSuspension result = useCase.revoke("SUS-3", "admin123");

        assertEquals(SuspensionStatus.REVOKED, result.getStatus());
        assertEquals("admin123", result.getAdminId());
        assertNotNull(result.getUpdatedAt());
    }
}

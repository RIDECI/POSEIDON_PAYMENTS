package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.application.service.UpdateSuspensionUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateSuspensionUseCaseImplTest {

    private PaymentSuspensionRepositoryPort repository;
    private UpdateSuspensionUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentSuspensionRepositoryPort.class);
        useCase = new UpdateSuspensionUseCaseImpl(repository);
    }

    @Test
    void update_suspensionNotFound_throwsException() {
        when(repository.findById("SUS-1")).thenReturn(Optional.empty());

        Supplier<PaymentSuspension> action = () -> useCase.update("SUS-1", "New reason", LocalDateTime.now(),
                "admin-1");

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, action::get);

        assertEquals("Suspension not found: SUS-1", ex.getMessage());
    }

    @Test
    void update_revokedSuspension_throwsException() {
        PaymentSuspension suspended = PaymentSuspension.builder()
                .id("SUS-2")
                .status(SuspensionStatus.REVOKED)
                .build();

        when(repository.findById("SUS-2")).thenReturn(Optional.of(suspended));

        Supplier<PaymentSuspension> action = () -> useCase.update("SUS-2", "reason", LocalDateTime.now(), "admin-1");

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, action::get);

        assertEquals("Cannot update a revoked suspension", ex.getMessage());
    }

    @Test
    void update_successful_updateFields() {
        LocalDateTime newExpiry = LocalDateTime.now().plusDays(3);
        PaymentSuspension suspended = PaymentSuspension.builder()
                .id("SUS-3")
                .status(SuspensionStatus.ACTIVE)
                .reason("old reason")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
        when(repository.findById("SUS-3")).thenReturn(Optional.of(suspended));
        when(repository.save(any(PaymentSuspension.class))).thenAnswer(i -> i.getArguments()[0]);

        PaymentSuspension updated = useCase.update("SUS-3", "new reason", newExpiry, "admin-123");

        assertEquals("new reason", updated.getReason());
        assertEquals(newExpiry, updated.getExpiresAt());
        assertEquals("admin-123", updated.getAdminId());
        assertNotNull(updated.getUpdatedAt());
        assertEquals(SuspensionStatus.ACTIVE, updated.getStatus());
    }

    @Test
    void update_nullFields_onlyUpdatesAdminAndUpdatedAt() {
        PaymentSuspension suspended = PaymentSuspension.builder()
                .id("SUS-4")
                .status(SuspensionStatus.ACTIVE)
                .reason("reason")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
        when(repository.findById("SUS-4")).thenReturn(Optional.of(suspended));
        when(repository.save(any(PaymentSuspension.class))).thenAnswer(i -> i.getArguments()[0]);

        PaymentSuspension updated = useCase.update("SUS-4", null, null, "admin-456");

        assertEquals("reason", updated.getReason());
        assertNotNull(updated.getUpdatedAt());
        assertEquals("admin-456", updated.getAdminId());
        assertEquals(SuspensionStatus.ACTIVE, updated.getStatus());
    }
}

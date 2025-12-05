package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.PaymentSuspension;


public interface UpdateSuspensionUseCase {
    PaymentSuspension update(String id, String reason, java.time.LocalDateTime expiresAt, String adminId);
}
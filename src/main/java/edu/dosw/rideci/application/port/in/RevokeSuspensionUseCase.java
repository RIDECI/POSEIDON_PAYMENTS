package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.PaymentSuspension;

public interface RevokeSuspensionUseCase {
    PaymentSuspension revoke(String id, String adminId);
}
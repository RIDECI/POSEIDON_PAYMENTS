package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.PaymentSuspension;


public interface CreateSuspensionUseCase {
    PaymentSuspension create(PaymentSuspension suspension);
}


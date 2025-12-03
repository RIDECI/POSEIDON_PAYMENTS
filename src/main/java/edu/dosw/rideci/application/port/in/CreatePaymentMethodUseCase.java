package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.PaymentMethod;

public interface CreatePaymentMethodUseCase {
    PaymentMethod create(PaymentMethod method);
}

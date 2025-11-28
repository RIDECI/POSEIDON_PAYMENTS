package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

public interface RegisterPaymentFailureUseCase {
    Transaction registerFailure(String paymentId, String errorMessage, String errorCode);
}

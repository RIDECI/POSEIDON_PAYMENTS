package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

public interface CancelPaymentUseCase {
    Transaction cancel(String paymentId);
}

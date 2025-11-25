package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Refund;

public interface RefundPaymentUseCase {
    Refund refundPayment(String transactionId, Double amount, String reason);
}

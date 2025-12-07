package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.CashPaymentConfirmation;

public interface ConfirmCashPaymentUseCase {
    CashPaymentConfirmation confirm(String transactionId, String driverId, String observations);
}

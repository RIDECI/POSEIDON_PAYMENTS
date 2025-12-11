package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.PaymentReceipt;

public interface GeneratePaymentReceiptUseCase {
    PaymentReceipt generate(String transactionId, String driverId);
}

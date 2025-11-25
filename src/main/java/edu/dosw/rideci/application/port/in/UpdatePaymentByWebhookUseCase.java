package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

public interface UpdatePaymentByWebhookUseCase {
    Transaction updateByWebhook(String provider, String paymentId, String status, 
                               String receiptCode, String errorMessage, String metadata);
}

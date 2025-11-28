package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

public interface ProcessWebhookUseCase {
    Transaction processWebhook(String provider, String transactionId, String externalReference, 
                              String status, Double amount, String paymentMethod, String metadata);
}

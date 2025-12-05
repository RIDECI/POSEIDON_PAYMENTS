package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.ProcessWebhookUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessWebhookUseCaseImpl implements ProcessWebhookUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public Transaction processWebhook(String provider, String transactionId, String externalReference,
                                     String status, Double amount, String paymentMethod, String metadata) {
        log.info("Processing webhook from provider: {} for transaction: {}", provider, transactionId);
        
        if (transactionId == null || transactionId.isEmpty()) {
            log.error("Transaction ID is required for webhook processing");
            throw new RideciBusinessException("Transaction ID is required");
        }

        Transaction payment = paymentRepositoryPort.findById(transactionId)
                .orElseThrow(() -> {
                    log.error("Payment not found for webhook: {}", transactionId);
                    return new RideciBusinessException("Payment not found: " + transactionId);
                });

        log.info("Current payment status: {} - Webhook status: {}", payment.getStatus(), status);

        TransactionStatus newStatus = mapProviderStatus(provider, status);
        
        if (newStatus != null && !payment.getStatus().equals(newStatus)) {
            payment.setStatus(newStatus);
            log.info("Payment status updated from webhook: {} -> {}", transactionId, newStatus);
        }

        if (metadata != null && !metadata.isEmpty()) {
            payment.setExtra(metadata);
        }

        Transaction updatedPayment = paymentRepositoryPort.save(payment);
        log.info("Webhook processed successfully for payment: {}", transactionId);
        
        return updatedPayment;
    }

    private TransactionStatus mapProviderStatus(String provider, String status) {
        if (status == null) {
            return null;
        }

        String normalizedStatus = status.toUpperCase();
        
        return switch (normalizedStatus) {
            case "PENDING", "PENDING_PAYMENT", "WAITING" -> TransactionStatus.PENDING;
            case "AUTHORIZED", "PRE_AUTHORIZED" -> TransactionStatus.AUTHORIZED;
            case "PROCESSING", "IN_PROCESS" -> TransactionStatus.PROCESSING;
            case "APPROVED", "ACCEPTED" -> TransactionStatus.APPROVED;
            case "COMPLETED", "SUCCESS", "PAID" -> TransactionStatus.COMPLETED;
            case "FAILED", "REJECTED", "ERROR" -> TransactionStatus.FAILED;
            case "REFUNDED", "REFUND" -> TransactionStatus.REFUNDED;
            case "CANCELLED", "CANCELED" -> TransactionStatus.CANCELLED;
            default -> {
                log.warn("Unknown status from provider {}: {}", provider, status);
                yield null;
            }
        };
    }
}

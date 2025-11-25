package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.UpdatePaymentByWebhookUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdatePaymentByWebhookUseCaseImpl implements UpdatePaymentByWebhookUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public Transaction updateByWebhook(String provider, String paymentId, String status,
                                      String receiptCode, String errorMessage, String metadata) {
        log.info("Updating payment {} via webhook from provider: {}", paymentId, provider);
        
        Transaction payment = paymentRepositoryPort.findById(paymentId)
                .orElseThrow(() -> {
                    log.error("Payment not found for webhook update: {}", paymentId);
                    return new RideciBusinessException("Payment not found: " + paymentId);
                });

        log.info("Current payment status: {}, requested status: {}", payment.getStatus(), status);

        // Validar que el pago no esté en estado final
        if (payment.getStatus() == TransactionStatus.COMPLETED) {
            log.warn("Cannot update completed payment: {}", paymentId);
            throw new RideciBusinessException("Cannot update completed payment");
        }

        if (payment.getStatus() == TransactionStatus.REFUNDED) {
            log.warn("Cannot update refunded payment: {}", paymentId);
            throw new RideciBusinessException("Cannot update refunded payment");
        }

        // Actualizar estado si se proporciona
        if (status != null && !status.isEmpty()) {
            TransactionStatus newStatus = mapProviderStatus(provider, status);
            if (newStatus != null) {
                payment.setStatus(newStatus);
                log.info("Payment status updated: {} -> {}", paymentId, newStatus);
            }
        }

        // Actualizar código de recibo si se proporciona
        if (receiptCode != null && !receiptCode.isEmpty()) {
            payment.setReceiptCode(receiptCode);
            log.info("Receipt code updated for payment: {}", paymentId);
        }

        // Actualizar mensaje de error si se proporciona
        if (errorMessage != null && !errorMessage.isEmpty()) {
            payment.setErrorMessage(errorMessage);
            log.warn("Error message set for payment {}: {}", paymentId, errorMessage);
        }

        // Actualizar metadata adicional
        if (metadata != null && !metadata.isEmpty()) {
            payment.setExtra(metadata);
        }

        Transaction updatedPayment = paymentRepositoryPort.save(payment);
        log.info("Payment {} updated successfully via webhook", paymentId);
        
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

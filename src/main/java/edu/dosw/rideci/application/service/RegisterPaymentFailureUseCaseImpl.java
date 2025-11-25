package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.RegisterPaymentFailureUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterPaymentFailureUseCaseImpl implements RegisterPaymentFailureUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private static final int MAX_ATTEMPTS = 3;

    @Override
    public Transaction registerFailure(String paymentId, String errorMessage, String errorCode) {
        log.error("Registering payment failure for payment: {} - Error: {} (Code: {})", 
                  paymentId, errorMessage, errorCode);
        
        Transaction payment = paymentRepositoryPort.findById(paymentId)
                .orElseThrow(() -> new RideciBusinessException("Payment not found: " + paymentId));

        // Incrementar intentos
        int currentAttempts = payment.getAttempts() != null ? payment.getAttempts() : 0;
        payment.setAttempts(currentAttempts + 1);

        // Actualizar mensaje de error
        String fullErrorMessage = String.format("[%s] %s (Attempt %d/%d)", 
                                                errorCode, errorMessage, payment.getAttempts(), MAX_ATTEMPTS);
        payment.setErrorMessage(fullErrorMessage);

        // Si alcanzó el máximo de intentos, marcar como FAILED
        if (payment.getAttempts() >= MAX_ATTEMPTS) {
            log.warn("Payment {} has reached maximum attempts ({}). Marking as FAILED", 
                     paymentId, MAX_ATTEMPTS);
            payment.setStatus(TransactionStatus.FAILED);
        } else {
            log.info("Payment {} failure registered. Attempts: {}/{}", 
                     paymentId, payment.getAttempts(), MAX_ATTEMPTS);
        }

        return paymentRepositoryPort.save(payment);
    }
}

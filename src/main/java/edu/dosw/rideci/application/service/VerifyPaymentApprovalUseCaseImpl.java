package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.VerifyPaymentApprovalUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyPaymentApprovalUseCaseImpl implements VerifyPaymentApprovalUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public Optional<Transaction> verifyApproval(String paymentId) {
        log.info("Verifying approval status for payment: {}", paymentId);
        
        Optional<Transaction> payment = paymentRepositoryPort.findById(paymentId);
        
        if (payment.isPresent()) {
            log.info("Payment {} found with status: {}", paymentId, payment.get().getStatus());
        } else {
            log.warn("Payment {} not found", paymentId);
        }
        
        return payment;
    }
}

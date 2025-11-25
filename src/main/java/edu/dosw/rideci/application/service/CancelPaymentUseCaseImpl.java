package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CancelPaymentUseCase;
import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.AuditAction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelPaymentUseCaseImpl implements CancelPaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final CreateAuditLogUseCase createAuditLogUseCase;

    @Override
    public Transaction cancel(String paymentId) {

        Transaction payment = paymentRepositoryPort.findById(paymentId)
                .orElseThrow(() -> new RideciBusinessException("Payment not found: " + paymentId));

        TransactionStatus previousStatus = payment.getStatus();

        if (payment.getStatus() == TransactionStatus.COMPLETED) {
            throw new RideciBusinessException("Cannot cancel a completed payment");
        }

        if (payment.getStatus() == TransactionStatus.CANCELLED) {
            throw new RideciBusinessException("Payment is already cancelled");
        }

        if (payment.getStatus() == TransactionStatus.REFUNDED) {
            throw new RideciBusinessException("Cannot cancel a refunded payment");
        }

        payment.setStatus(TransactionStatus.CANCELLED);

        Transaction cancelledPayment = paymentRepositoryPort.save(payment);
        
        // Registrar auditoría
        try {
            createAuditLogUseCase.createAuditLog(AuditLog.builder()
                    .entityType("Transaction")
                    .entityId(cancelledPayment.getId())
                    .action(AuditAction.CANCEL)
                    .userId(cancelledPayment.getPassengerId())
                    .userName("User")
                    .description(String.format("Payment cancelled for booking %s", 
                                              cancelledPayment.getBookingId()))
                    .previousState(String.format("Status: %s", previousStatus))
                    .newState(String.format("Status: %s", cancelledPayment.getStatus()))
                    .build());
        } catch (Exception e) {
            log.error("Failed to create audit log for payment cancellation: {}", cancelledPayment.getId(), e);
        }

        return cancelledPayment;
    }
}

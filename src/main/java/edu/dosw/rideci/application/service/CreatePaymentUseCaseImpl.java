package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreatePaymentUseCase;
import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.AuditAction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final CreateAuditLogUseCase createAuditLogUseCase;

    @Override
    public Transaction create(Transaction tx) {
        tx.setStatus(TransactionStatus.PENDING);
        tx.setCreatedAt(LocalDateTime.now());
        Transaction savedTransaction = paymentRepositoryPort.save(tx);
        
        try {
            createAuditLogUseCase.createAuditLog(AuditLog.builder()
                    .entityType("Transaction")
                    .entityId(savedTransaction.getId())
                    .action(AuditAction.CREATE)
                    .userId(savedTransaction.getPassengerId())
                    .userName("Passenger")
                    .description(String.format("Payment created for booking %s - Amount: %.2f", 
                                              savedTransaction.getBookingId(), 
                                              savedTransaction.getAmount()))
                    .newState(String.format("Status: %s, Amount: %.2f, Method: %s", 
                                           savedTransaction.getStatus(), 
                                           savedTransaction.getAmount(),
                                           savedTransaction.getPaymentMethod()))
                    .build());
        } catch (Exception e) {
            log.error("Failed to create audit log for payment creation: {}", savedTransaction.getId(), e);
        }
        
        return savedTransaction;
    }
    
}

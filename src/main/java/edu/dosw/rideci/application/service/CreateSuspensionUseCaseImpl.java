package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreateSuspensionUseCase;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateSuspensionUseCaseImpl implements CreateSuspensionUseCase {

    private final PaymentSuspensionRepositoryPort repository;
    private final PaymentRepositoryPort paymentRepository; 
    @Override
    public PaymentSuspension create(PaymentSuspension suspension) {

        if (suspension.getTransactionId() == null) {
            throw new RideciBusinessException("transactionId is required");
        }

        Transaction tx = paymentRepository.findById(suspension.getTransactionId())
                .orElseThrow(() -> new RideciBusinessException(
                        "Transaction not found: " + suspension.getTransactionId()
                ));

        TransactionStatus status = tx.getStatus();

        if (status == TransactionStatus.PROCESSING
                || status == TransactionStatus.COMPLETED
                || status == TransactionStatus.APPROVED
                || status == TransactionStatus.FAILED
                || status == TransactionStatus.REFUNDED) {

            throw new RideciBusinessException(
                    "Cannot create suspension for transaction in status: " + status
            );
        }

        var existingActive = repository.findActiveByTransactionId(suspension.getTransactionId());
        if (existingActive.isPresent()) {
            throw new RideciBusinessException("An active suspension already exists for this transaction");
        }

        var now = LocalDateTime.now();
        PaymentSuspension toSave = PaymentSuspension.builder()
                .id("SUS-" + UUID.randomUUID())
                .transactionId(suspension.getTransactionId())
                .reason(suspension.getReason())
                .status(SuspensionStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .expiresAt(suspension.getExpiresAt())
                .adminId(suspension.getAdminId())
                .build();

        return repository.save(toSave);
    }
}

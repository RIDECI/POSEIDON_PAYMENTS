package edu.dosw.rideci.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GeneratePaymentReceiptUseCase;
import edu.dosw.rideci.application.port.out.PaymentReceiptRepositoryPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratePaymentReceiptUseCaseImpl implements GeneratePaymentReceiptUseCase {

    private final PaymentRepositoryPort paymentRepo;
    private final PaymentReceiptRepositoryPort receiptRepo;

    @Override
    public PaymentReceipt generate(String transactionId, String driverId) {

        var tx = paymentRepo.findById(transactionId)
                .orElseThrow(() -> new RideciBusinessException("Transaction not found"));

        if (tx.getStatus() != TransactionStatus.COMPLETED) {
            throw new RideciBusinessException("Receipt can only be generated for COMPLETED payments");
        }

        receiptRepo.findByTransactionId(transactionId)
                .ifPresent(r -> {
                    throw new RideciBusinessException("Receipt already exists for this transaction");
                });

        var receipt = PaymentReceipt.builder()
                .id("REC-" + UUID.randomUUID())
                .transactionId(transactionId)
                .receiptCode("R-" + tx.getId())
                .passengerId(tx.getPassengerId())
                .driverId(driverId)               
                .bookingId(tx.getBookingId())
                .amount(tx.getAmount())
                .paymentMethod(tx.getPaymentMethod().name())
                .transactionMethod("STANDARD")
                .issuedAt(LocalDateTime.now())
                .build();

        return receiptRepo.save(receipt);
    }
}

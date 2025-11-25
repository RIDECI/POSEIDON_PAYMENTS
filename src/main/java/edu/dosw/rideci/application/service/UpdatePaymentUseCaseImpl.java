package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.UpdatePaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.controller.dto.Request.UpdatePaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePaymentUseCaseImpl implements UpdatePaymentUseCase {

    private final PaymentRepositoryPort repository;

    @Override
    public Transaction updatePartial(String id, UpdatePaymentRequest req) {

        Transaction tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (tx.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Solo se pueden modificar pagos con estado PENDING"
            );
        }

        if (req.getAmount() != null) {
            tx.setAmount(req.getAmount());
        }

        if (req.getPaymentMethod() != null) {
            tx.setPaymentMethod(req.getPaymentMethod());
        }

        if (req.getExtra() != null) {
            tx.setExtra(req.getExtra());
        }

        if (req.getReceiptCode() != null) {

            if (tx.getPaymentMethod() == PaymentMethodType.CASH
                    || req.getPaymentMethod() == PaymentMethodType.CASH) {
                throw new IllegalArgumentException(
                        "receiptCode no permitido para pagos CASH"
                );
            }

            tx.setReceiptCode(req.getReceiptCode());
        }

        return repository.save(tx);
    }
}

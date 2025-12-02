package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CompletePaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompletePaymentUseCaseImpl implements CompletePaymentUseCase {

    private final PaymentRepositoryPort repository;

    @Override
    public Transaction complete(String id) {

        Transaction tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (tx.getStatus() == TransactionStatus.COMPLETED) {
            throw new IllegalStateException("El pago ya fue completado previamente");
        }

        if (tx.getStatus() != TransactionStatus.APPROVED) {
            throw new IllegalStateException("Solo se pueden completar pagos en estado APPROVED");
        }

        if (tx.getAmount() == null || tx.getAmount() <= 0) {
            throw new IllegalArgumentException("Monto inválido para completar el pago");
        }

        if (tx.getPaymentMethod() == PaymentMethodType.CASH) {
        } else {
            if (tx.getReceiptCode() == null || tx.getReceiptCode().isBlank()) {
                throw new IllegalArgumentException("receiptCode requerido para pagos digitales");
            }
        }

        tx.setStatus(TransactionStatus.COMPLETED);

        tx.setExtra((tx.getExtra() != null ? tx.getExtra() : "") + "|COMPLETED:true");

        return repository.save(tx);
    }
}

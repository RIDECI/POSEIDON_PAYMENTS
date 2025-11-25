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

        // 1. Debe estar APPROVED
        if (tx.getStatus() != TransactionStatus.APPROVED) {
            throw new IllegalStateException("Solo se pueden completar pagos en estado APPROVED");
        }

        // 2. El monto debe ser válido
        if (tx.getAmount() == null || tx.getAmount() <= 0) {
            throw new IllegalArgumentException("Monto inválido para completar el pago");
        }

        // 3. CASH requiere lógica especial
        if (tx.getPaymentMethod() == PaymentMethodType.CASH) {
            // CASH no usa receiptCode
        } else {
            // 4. Digitales requieren receiptCode válido
            if (tx.getReceiptCode() == null || tx.getReceiptCode().isBlank()) {
                throw new IllegalArgumentException("receiptCode requerido para pagos digitales");
            }
        }

        // 5. Evitar completar dos veces
        if (tx.getStatus() == TransactionStatus.COMPLETED) {
            throw new IllegalStateException("El pago ya fue completado previamente");
        }

        // 6. Finalmente actualizar estado
        tx.setStatus(TransactionStatus.COMPLETED);

        // Añadimos metadata
        tx.setExtra((tx.getExtra() != null ? tx.getExtra() : "") + "|COMPLETED:true");

        return repository.save(tx);
    }
}

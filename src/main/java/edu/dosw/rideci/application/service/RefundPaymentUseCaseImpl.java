package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.RefundPaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundPaymentUseCaseImpl implements RefundPaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public Refund refundPayment(String transactionId, Double amount, String reason) {

        // 1. Verificar si existe la transacción
        Transaction tx = paymentRepositoryPort.findById(transactionId)
                .orElseThrow(() -> new RideciBusinessException("Transaction not found: " + transactionId));

        // 2. Solo se reembolsan transacciones COMPLETED
        if (tx.getStatus() != TransactionStatus.COMPLETED) {
            throw new RideciBusinessException(
                    "Only COMPLETED transactions can be refunded. Current status: " + tx.getStatus()
            );
        }

        // 3. Validar si ya existe un refund para esa transacción
        Refund existing = refundRepositoryPort.findByTransactionId(transactionId);
        if (existing != null) {
            throw new RideciBusinessException("This transaction already has a refund request");
        }

        // 4. Validar que no exceda el monto pagado
        if (amount <= 0) {
            throw new RideciBusinessException("Refund amount must be greater than zero");
        }

        if (amount > tx.getAmount()) {
            throw new RideciBusinessException(
                    "Refund amount (" + amount + ") exceeds transaction amount (" + tx.getAmount() + ")"
            );
        }

        // 5. Crear Refund domain
        Refund refund = Refund.builder()
                .id("REF-" + UUID.randomUUID())
                .transactionId(tx.getId())
                .bookingId(tx.getBookingId())
                .passengerId(tx.getPassengerId())
                .refundedAmount(amount)
                .reason(reason)
                .status(RefundStatus.REQUESTED)
                .requestAt(LocalDateTime.now())
                .build();

        // 6. Guardar refund
        return refundRepositoryPort.save(refund);
    }
}

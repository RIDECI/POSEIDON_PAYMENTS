package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.ApprovePaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentApprovalPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApprovePaymentUseCaseImpl implements ApprovePaymentUseCase {

    private final PaymentRepositoryPort repository;
    private final PaymentApprovalPort approvalPort;

    @Override
    public Transaction approve(String id) {

        Transaction tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Regla #2: Debe estar procesado
        if (tx.getStatus() != TransactionStatus.PROCESSING) {
            throw new IllegalStateException("Solo se pueden aprobar pagos procesados");
        }

        // Regla #3: Debe tener recibo válido
        if (tx.getReceiptCode() == null || tx.getReceiptCode().isBlank()) {
            throw new IllegalArgumentException("receiptCode requerido para aprobar el pago");
        }

        // Regla #4: CASH no se aprueba
        if (tx.getPaymentMethod() == PaymentMethodType.CASH) {
            throw new IllegalStateException("Los pagos CASH no requieren aprobación");
        }

        // Regla #5: No aprobar dos veces
        if (tx.getStatus() == TransactionStatus.AUTHORIZED) {
            throw new IllegalStateException("Solo se pueden aprobar una vez un");
        }

        // Regla #6: Monto válido
        if (tx.getAmount() == null || tx.getAmount() <= 0) {
            throw new IllegalStateException("Monto inválido para aprobación");
        }

        // Regla #7: bookingId obligatorio
        if (tx.getBookingId() == null) {
            throw new IllegalArgumentException("La transacción no tiene bookingId asignado");
        }

        // Marcar como aprobado en metadatos (sin cambiar status)
        tx.setStatus(TransactionStatus.APPROVED);

        approvalPort.logApproval(tx.getId());

        return repository.save(tx);
    }
}

package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.ApprovePaymentUseCase;
import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.application.port.out.PaymentApprovalPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.AuditAction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovePaymentUseCaseImpl implements ApprovePaymentUseCase {

    private final PaymentRepositoryPort repository;
    private final PaymentApprovalPort approvalPort;
    private final CreateAuditLogUseCase createAuditLogUseCase;

    @Override
    public Transaction approve(String id) {

        Transaction tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        TransactionStatus previousStatus = tx.getStatus();

        // Regla #2: Debe estar procesado
        if (tx.getStatus() != TransactionStatus.PROCESSING) {
            throw new IllegalStateException("Solo se pueden aprobar pagos procesados");
        }

        // Regla #3: Debe tener recibo vÃ¡lido
        if (tx.getReceiptCode() == null || tx.getReceiptCode().isBlank()) {
            throw new IllegalArgumentException("receiptCode requerido para aprobar el pago");
        }

        // Regla #4: CASH no se aprueba
        if (tx.getPaymentMethod() == PaymentMethodType.CASH) {
            throw new IllegalStateException("Los pagos CASH no requieren aprobaciÃ³n");
        }

        // Regla #5: No aprobar dos veces
        if (tx.getStatus() == TransactionStatus.AUTHORIZED) {
            throw new IllegalStateException("Solo se pueden aprobar una vez un");
        }

        // Regla #6: Monto vÃ¡lido
        if (tx.getAmount() == null || tx.getAmount() <= 0) {
            throw new IllegalStateException("Monto invÃ¡lido para aprobaciÃ³n");
        }

        // Regla #7: bookingId obligatorio
        if (tx.getBookingId() == null) {
            throw new IllegalArgumentException("La transacciÃ³n no tiene bookingId asignado");
        }

        // Marcar como aprobado en metadatos (sin cambiar status)
        tx.setStatus(TransactionStatus.APPROVED);

        approvalPort.logApproval(tx.getId());

        Transaction approvedTx = repository.save(tx);
        
        // Registrar auditorÃ­a
        try {
            createAuditLogUseCase.createAuditLog(AuditLog.builder()
                    .entityType("Transaction")
                    .entityId(approvedTx.getId())
                    .action(AuditAction.APPROVE)
                    .userId("SYSTEM")
                    .userName("Payment System")
                    .description(String.format("Payment approved for booking %s - Receipt: %s", 
                                              approvedTx.getBookingId(), 
                                              approvedTx.getReceiptCode()))
                    .previousState(String.format("Status: %s", previousStatus))
                    .newState(String.format("Status: %s, Receipt: %s", 
                                           approvedTx.getStatus(),
                                           approvedTx.getReceiptCode()))
                    .build());
        } catch (Exception e) {
            log.error("Failed to create audit log for payment approval: {}", approvedTx.getId(), e);
        }

        return approvedTx;
    }
}

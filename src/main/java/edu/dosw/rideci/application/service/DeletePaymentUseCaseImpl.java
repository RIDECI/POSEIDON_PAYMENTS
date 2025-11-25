package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.DeletePaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePaymentUseCaseImpl implements DeletePaymentUseCase {

    private final PaymentRepositoryPort repository;

    @Override
    public boolean deleteById(String id) {

        if (!repository.existsById(id)) {
            return false;
        }

        // FUTURA VALIDACIÓN (solo eliminar transacciones FAILED)
        //
        // Transaction tx = repository.findById(id)
        //         .orElse(null);
        //
        // if (tx == null) {
        //     return false;
        // }
        //
        // if (tx.getStatus() != TransactionStatus.FAILED) {
        //     throw new RideciBusinessException(
        //             "Solo se pueden eliminar pagos con estado FAILED"
        //     );
        // }

        repository.deleteById(id);
        return true;
    }
}

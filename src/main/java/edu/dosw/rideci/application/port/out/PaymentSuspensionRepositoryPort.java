package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.PaymentSuspension;

import java.util.List;
import java.util.Optional;

public interface PaymentSuspensionRepositoryPort {

    PaymentSuspension save(PaymentSuspension suspension);

    Optional<PaymentSuspension> findById(String id);

    Optional<PaymentSuspension> findActiveByTransactionId(String transactionId);

    List<PaymentSuspension> findAllByTransactionId(String transactionId);

    List<PaymentSuspension> findAll();

    void deleteById(String id);

    boolean existsById(String id);
}

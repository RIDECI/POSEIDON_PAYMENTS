package edu.dosw.rideci.application.port.out;

import java.util.List;
import java.util.Optional;

import edu.dosw.rideci.domain.model.CashPaymentConfirmation;

public interface CashPaymentConfirmationRepositoryPort {

    CashPaymentConfirmation save(CashPaymentConfirmation confirmation);

    Optional<CashPaymentConfirmation> findByTransactionId(String transactionId);

    Optional<CashPaymentConfirmation> findById(String id);

    List<CashPaymentConfirmation> findAll();
}

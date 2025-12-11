package edu.dosw.rideci.application.port.in;

import java.util.List;
import edu.dosw.rideci.domain.model.CashPaymentConfirmation;

public interface CashPaymentQueryUseCase {

    CashPaymentConfirmation getById(String id);

    CashPaymentConfirmation getByTransactionId(String transactionId);

    List<CashPaymentConfirmation> getAll();
}

package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;

import java.util.List;

public interface GetTransactionsByPaymentMethodUseCase {
    List<Transaction> findByPaymentMethod(PaymentMethodType paymentMethodType);
}

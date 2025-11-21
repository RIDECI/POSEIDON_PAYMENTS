package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

public class CashPayment implements PaymentStrategy {
    @Override
    public Transaction processPayment(Transaction transaction) {
        transaction.setStatus(TransactionStatus.PENDING_CASH);
        return transaction;
    }
}

package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

public class NequiPayment implements PaymentStrategy {

    @Override
    public Transaction processPayment(Transaction transaction) {
        transaction.setStatus(TransactionStatus.PROCESSING);
        try {
            // Aquí iría la integración real con Nequi (client HTTP)
            transaction.setReceiptCode("NEQUI-" + transaction.getId());
            transaction.setStatus(TransactionStatus.COMPLETED);
        } catch (Exception ex) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setErrorMessage(ex.getMessage());
        }
        return transaction;
    }
}

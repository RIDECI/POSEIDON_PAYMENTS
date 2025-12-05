package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

public class NequiPayment implements PaymentStrategy {

    @Override
    public Transaction processPayment(Transaction tx) {

        String phone = tx.getReceiptCode(); 

        switch (phone) {

            case "3001234567": 
                tx.setStatus(TransactionStatus.PROCESSING);
                tx.setReceiptCode("NEQUI-" + tx.getId());
                break;

            case "3999999999":
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INSUFFICIENT_FUNDS");
                break;

            case "3111111111": 
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INVALID_TOKEN");
                break;

            default: 
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("REJECTED");
                break;
        }
        return tx;
    }
}

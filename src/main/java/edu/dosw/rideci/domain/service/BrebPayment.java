package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

public class BrebPayment implements PaymentStrategy {

    @Override
    public Transaction processPayment(Transaction tx) {

        String code = tx.getReceiptCode();

        switch (code) {
            case "00":
                tx.setStatus(TransactionStatus.PROCESSING);
                tx.setReceiptCode("BRE_B-" + tx.getId());
                break;
            case "51":
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INSUFFICIENT_FUNDS");
                break;
            case "54":
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("EXPIRED_CARD");
                break;
            case "57":
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("NOT_ALLOWED");
                break;
            case "91":
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("BANK_UNAVAILABLE");
                break;
            default:
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INVALID_CODE");
                break;
        }

        return tx;
    }
}


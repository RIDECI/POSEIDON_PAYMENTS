package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

public class NequiPayment implements PaymentStrategy {

    @Override
    public Transaction processPayment(Transaction tx) {

        String phone = tx.getReceiptCode(); // recibimos número de celular simulado

        switch (phone) {

            case "3001234567": // éxito
                tx.setStatus(TransactionStatus.PROCESSING);
                tx.setReceiptCode("NEQUI-" + tx.getId());
                break;

            case "3999999999": // sin fondos
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INSUFFICIENT_FUNDS");
                break;

            case "3111111111": // token inválido
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INVALID_TOKEN");
                break;

            default: // rechazado
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("REJECTED");
                break;
        }
        return tx;
    }
}

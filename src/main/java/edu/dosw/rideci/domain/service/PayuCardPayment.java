package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

public class PayuCardPayment implements PaymentStrategy {

    @Override
    public Transaction processPayment(Transaction tx) {

        String cardNumber = tx.getReceiptCode(); // reutilizamos receiptCode para almacenar temporalmente el número de tarjeta

        if (cardNumber == null) {
            tx.setStatus(TransactionStatus.FAILED);
            tx.setErrorMessage("Card number missing");
            return tx;
        }

        // REGLAS OFICIALES PAYU SANDBOX
        switch (cardNumber) {
            case "4030 0000 1000 0009": // Aprobado
                tx.setStatus(TransactionStatus.PROCESSING);
                tx.setReceiptCode("PAYU-" + tx.getId());
                break;

            case "5431111111111111": // Fondos insuficientes
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INSUFFICIENT_FUNDS");
                break;

            case "5123456789012346": // Tarjeta expirada
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("EXPIRED_CARD");
                break;

            case "4111111111111111": // Rechazo general
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("REJECTED");
                break;

            default:
                tx.setStatus(TransactionStatus.FAILED);
                tx.setErrorMessage("INVALID_CARD");
                break;
        }

        return tx;
    }
}

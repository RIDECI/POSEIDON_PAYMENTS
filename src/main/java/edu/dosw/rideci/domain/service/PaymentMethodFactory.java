package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.enums.PaymentMethodType;

public class PaymentMethodFactory {

    public PaymentStrategy createStrategy(PaymentMethodType type) {
        return switch (type) {
            case CASH -> new CashPayment();
            case NEQUI -> new NequiPayment();
            default -> throw new IllegalArgumentException("Unsupported payment method: " + type);
        };
    }
}

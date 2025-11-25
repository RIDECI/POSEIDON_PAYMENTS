package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodFactory {

    public PaymentStrategy createStrategy(PaymentMethodType type) {

        return switch (type) {
            case CASH -> new CashPayment();
            case NEQUI -> new NequiPayment();
            case CREDIT_CARD_PAYU -> new PayuCardPayment();
            case BRE_B_key -> new BrebPayment();
            default -> throw new IllegalArgumentException("Unsupported method");
        };
    }

}

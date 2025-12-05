package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.CreditCard;

public interface CreateCreditCardUseCase {
    CreditCard create(CreditCard card);
}

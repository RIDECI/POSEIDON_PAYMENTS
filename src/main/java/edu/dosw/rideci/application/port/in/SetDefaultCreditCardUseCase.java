package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.CreditCard;

public interface SetDefaultCreditCardUseCase {
    CreditCard setDefault(String id);
}

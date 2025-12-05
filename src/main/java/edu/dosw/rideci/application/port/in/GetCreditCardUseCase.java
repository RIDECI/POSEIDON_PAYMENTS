package edu.dosw.rideci.application.port.in;

import java.util.List;

import edu.dosw.rideci.domain.model.CreditCard;

public interface GetCreditCardUseCase {

    List<CreditCard> findAll();

    List<CreditCard> findByUser(String userId);

    CreditCard findById(String id);

    CreditCard findDefault(String userId);
}

package edu.dosw.rideci.application.port.out;

import java.util.List;
import java.util.Optional;

import edu.dosw.rideci.domain.model.CreditCard;

public interface CreditCardRepositoryPort {

    CreditCard save(CreditCard card);

    Optional<CreditCard> findById(String id);

    List<CreditCard> findAll();

    List<CreditCard> findByUserId(String userId);

    Optional<CreditCard> findDefaultForUser(String userId);

    void clearDefaults(String userId); 

    void deleteById(String id);
}

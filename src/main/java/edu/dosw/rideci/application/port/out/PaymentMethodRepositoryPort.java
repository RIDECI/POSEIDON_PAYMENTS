package edu.dosw.rideci.application.port.out;

import java.util.List;
import java.util.Optional;

import edu.dosw.rideci.domain.model.PaymentMethod;

public interface PaymentMethodRepositoryPort {

    PaymentMethod save(PaymentMethod method);

    Optional<PaymentMethod> findById(String id);

    List<PaymentMethod> findByUserId(String userId);

    Optional<PaymentMethod> findDefaultForUser(String userId);

    void deleteById(String id);
}

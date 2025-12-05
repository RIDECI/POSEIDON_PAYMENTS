package edu.dosw.rideci.application.port.in;

import java.util.List;
import edu.dosw.rideci.domain.model.PaymentMethod;

public interface GetPaymentMethodsUseCase {
    List<PaymentMethod> getByUserId(String userId);
    PaymentMethod getById(String id);
    List<PaymentMethod> findAll();
}

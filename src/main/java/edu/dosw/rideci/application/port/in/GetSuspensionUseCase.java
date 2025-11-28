package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.PaymentSuspension;

import java.util.List;
import java.util.Optional;

public interface GetSuspensionUseCase {
    Optional<PaymentSuspension> getById(String id);
    List<PaymentSuspension> getAll();
}
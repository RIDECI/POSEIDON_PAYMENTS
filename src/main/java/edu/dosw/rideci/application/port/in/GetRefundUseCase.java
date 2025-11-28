package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Refund;

import java.util.List;
import java.util.Optional;

public interface GetRefundUseCase {

    Optional<Refund> getById(String id);

    List<Refund> getAll();
}

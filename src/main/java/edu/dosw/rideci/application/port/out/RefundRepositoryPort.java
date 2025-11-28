package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;

import java.util.List;
import java.util.Optional;

public interface RefundRepositoryPort {

    Refund save(Refund refund);

    Refund findByTransactionId(String transactionId);

    Optional<Refund> findById(String id);

    List<Refund> findAll();

    List<Refund> findByStatus(RefundStatus status);

    void deleteById(String id);

    boolean existsById(String id);
}

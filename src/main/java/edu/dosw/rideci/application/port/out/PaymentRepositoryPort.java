package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(String id);

    List<Transaction> findAll();

    List<Transaction> findByStatus(TransactionStatus status);

    void deleteById(String id);

    boolean existsById(String id);
}

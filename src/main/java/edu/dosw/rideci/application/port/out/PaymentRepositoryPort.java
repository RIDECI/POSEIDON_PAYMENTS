package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.Transaction;
import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(String id);
    List<Transaction> findAll();
    void deleteById(String id);
}

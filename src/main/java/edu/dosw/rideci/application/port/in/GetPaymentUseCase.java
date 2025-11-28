package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface GetPaymentUseCase {
    Optional<Transaction> getById(String id);
    List<Transaction> findAll();
}
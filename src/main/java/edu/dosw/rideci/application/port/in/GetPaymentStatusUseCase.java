package edu.dosw.rideci.application.port.in;

import java.util.List;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

public interface GetPaymentStatusUseCase {
    List<Transaction> findByStatus(TransactionStatus status);
}


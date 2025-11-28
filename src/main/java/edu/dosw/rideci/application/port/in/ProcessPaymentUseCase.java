package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

public interface ProcessPaymentUseCase {
    Transaction process(String id);
}


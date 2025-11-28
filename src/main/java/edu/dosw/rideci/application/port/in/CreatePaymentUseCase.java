package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

public interface CreatePaymentUseCase {
    Transaction create(Transaction transaction);
    
}

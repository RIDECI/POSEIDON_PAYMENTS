package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;

public interface AuthorizePaymentUseCase {
    Transaction authorize(String id, boolean authorized);
}

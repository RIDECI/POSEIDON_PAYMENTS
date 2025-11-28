package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.Transaction;

import java.util.Optional;

public interface AuthorizePaymentPort {
    Optional<Transaction> authorizePayment(String id, boolean authorized);
    boolean isAuthorized(String id);
}

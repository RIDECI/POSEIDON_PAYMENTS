package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Transaction;

public interface PaymentStrategy {
    Transaction processPayment(Transaction transaction);
}

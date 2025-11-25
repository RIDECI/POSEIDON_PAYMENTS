package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;
import java.util.List;

public interface GetActivePaymentsUseCase {
    List<Transaction> getActivePayments();
}

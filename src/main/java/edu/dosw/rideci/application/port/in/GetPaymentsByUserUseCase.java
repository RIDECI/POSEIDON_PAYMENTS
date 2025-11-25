package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;
import java.util.List;

public interface GetPaymentsByUserUseCase {
    List<Transaction> getByUserId(String userId);
}

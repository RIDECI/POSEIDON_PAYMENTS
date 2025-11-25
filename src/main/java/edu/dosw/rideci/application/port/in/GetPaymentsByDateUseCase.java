package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;
import java.time.LocalDate;
import java.util.List;

public interface GetPaymentsByDateUseCase {
    List<Transaction> getByDate(LocalDate date);
}

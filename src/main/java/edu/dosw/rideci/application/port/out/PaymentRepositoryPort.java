package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(String id);

    List<Transaction> findAll();

    List<Transaction> findByStatus(TransactionStatus status);

    List<Transaction> findByPaymentMethod(PaymentMethodType paymentMethodType);

    List<Transaction> findByBookingId(String bookingId);

    List<Transaction> findByPassengerId(String passengerId);

    List<Transaction> findByCreatedAtDate(LocalDate date);

    List<Transaction> findActivePayments();

    void deleteById(String id);

    boolean existsById(String id);
}

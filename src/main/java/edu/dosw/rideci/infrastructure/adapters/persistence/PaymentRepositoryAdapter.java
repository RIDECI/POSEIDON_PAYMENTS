package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.persistence.Entity.TransactionEntity;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.TransactionMapper;
import edu.dosw.rideci.infrastructure.persistence.Repository.TransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final TransactionJpaRepository repository;
    private final TransactionMapper mapper;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);
        TransactionEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return repository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Transaction> findAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        return repository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByBookingId(String bookingId) {
        return repository.findByBookingId(bookingId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByPassengerId(String passengerId) {
        return repository.findByPassengerId(passengerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByCreatedAtDate(LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        return repository.findByCreatedAtDate(dateTime)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findActivePayments() {
        return repository.findActivePayments()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByPaymentMethod(PaymentMethodType paymentMethodType) {
        return repository.findByPaymentMethod(paymentMethodType)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

}

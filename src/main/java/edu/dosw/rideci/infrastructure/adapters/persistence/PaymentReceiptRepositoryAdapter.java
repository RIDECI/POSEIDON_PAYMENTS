package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.PaymentReceiptRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.infrastructure.persistence.Repository.PaymentReceiptJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.PaymentReceiptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentReceiptRepositoryAdapter implements PaymentReceiptRepositoryPort {

    private final PaymentReceiptJpaRepository repo;
    private final PaymentReceiptMapper mapper;

    @Override
    public PaymentReceipt save(PaymentReceipt receipt) {
        return mapper.toDomain(repo.save(mapper.toEntity(receipt)));
    }

    @Override
    public Optional<PaymentReceipt> findById(String id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<PaymentReceipt> findByReceiptCode(String receiptCode) {
        return repo.findByReceiptCode(receiptCode).map(mapper::toDomain);
    }

    @Override
    public Optional<PaymentReceipt> findByTransactionId(String transactionId) {
        return repo.findByTransactionId(transactionId).map(mapper::toDomain);
    }

    @Override
    public List<PaymentReceipt> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }
}

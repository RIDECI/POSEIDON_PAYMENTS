package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.infrastructure.persistence.Entity.PaymentSuspensionEntity;
import edu.dosw.rideci.infrastructure.persistence.Repository.PaymentSuspensionJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.PaymentSuspensionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentSuspensionRepositoryAdapter implements PaymentSuspensionRepositoryPort {

    private final PaymentSuspensionJpaRepository jpa;
    private final PaymentSuspensionMapper mapper;

    @Override
    public PaymentSuspension save(PaymentSuspension suspension) {
        PaymentSuspensionEntity ent = mapper.toEntity(suspension);
        PaymentSuspensionEntity saved = jpa.save(ent);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PaymentSuspension> findById(String id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<PaymentSuspension> findActiveByTransactionId(String transactionId) {
        return jpa.findByTransactionIdAndStatus(transactionId, SuspensionStatus.ACTIVE)
                .map(mapper::toDomain);
    }

    @Override
    public List<PaymentSuspension> findAllByTransactionId(String transactionId) {
        return jpa.findByTransactionId(transactionId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<PaymentSuspension> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return jpa.existsById(id);
    }
}

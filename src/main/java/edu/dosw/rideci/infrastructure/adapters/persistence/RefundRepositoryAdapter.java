package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.infrastructure.persistence.Repository.RefundJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.RefundMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundRepositoryAdapter implements RefundRepositoryPort {

    private final RefundJpaRepository refundJpaRepository;
    private final RefundMapper refundMapper;

    @Override
    public Refund save(Refund refund) {
        return refundMapper.toDomain(
                refundJpaRepository.save(
                        refundMapper.toEntity(refund)));
    }

    @Override
    public Refund findByTransactionId(String transactionId) {
        return refundJpaRepository.findByTransactionId(transactionId)
                .map(refundMapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteById(String id) {
        refundJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return refundJpaRepository.existsById(id);
    }

    @Override
    public Optional<Refund> findById(String id) {
        return refundJpaRepository.findById(id)
                .map(refundMapper::toDomain);
    }

    @Override
    public List<Refund> findAll() {
        return refundJpaRepository.findAll().stream()
                .map(refundMapper::toDomain)
                .toList();
    }

    @Override
    public List<Refund> findByStatus(RefundStatus status) {
        return refundJpaRepository.findByStatus(status)
                .stream()
                .map(refundMapper::toDomain)
                .toList();
    }

}

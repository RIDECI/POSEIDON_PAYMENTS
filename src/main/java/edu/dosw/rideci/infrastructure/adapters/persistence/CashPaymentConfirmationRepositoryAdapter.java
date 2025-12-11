package edu.dosw.rideci.infrastructure.adapters.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.application.port.out.CashPaymentConfirmationRepositoryPort;
import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import edu.dosw.rideci.infrastructure.persistence.Repository.CashPaymentConfirmationJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.CashPaymentConfirmationMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CashPaymentConfirmationRepositoryAdapter implements CashPaymentConfirmationRepositoryPort {

    private final CashPaymentConfirmationJpaRepository repo;
    private final CashPaymentConfirmationMapper mapper;

    @Override
    public CashPaymentConfirmation save(CashPaymentConfirmation confirmation) {
        return mapper.toDomain(
                repo.save(mapper.toEntity(confirmation))
        );
    }

    @Override
    public Optional<CashPaymentConfirmation> findByTransactionId(String txId) {
        return repo.findByTransactionId(txId).map(mapper::toDomain);
    }

    @Override
    public Optional<CashPaymentConfirmation> findById(String id) {
        return repo.findById(id).map(mapper::toDomain);
    }
     @Override
    public List<CashPaymentConfirmation> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

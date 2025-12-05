package edu.dosw.rideci.infrastructure.adapters.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.infrastructure.persistence.Repository.PaymentMethodJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.PaymentMethodMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentMethodRepositoryAdapter implements PaymentMethodRepositoryPort {

    private final PaymentMethodJpaRepository repo;
    private final PaymentMethodMapper mapper;

    @Override
    public PaymentMethod save(PaymentMethod method) {
        return mapper.toDomain(repo.save(mapper.toEntity(method)));
    }

    @Override
    public Optional<PaymentMethod> findById(String id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<PaymentMethod> findByUserId(String userId) {
        return repo.findByUserId(userId).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public Optional<PaymentMethod> findDefaultForUser(String userId) {
        return repo.findDefaultByUserId(userId).map(mapper::toDomain);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }

    @Override
    public List<PaymentMethod> findAll() {
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

}

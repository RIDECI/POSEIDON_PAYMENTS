package edu.dosw.rideci.infrastructure.adapters.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.domain.model.BrebKey;
import edu.dosw.rideci.infrastructure.persistence.Repository.BrebKeyJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.BrebKeyMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BrebKeyRepositoryAdapter implements BrebKeyRepositoryPort {

    private final BrebKeyJpaRepository repo;
    private final BrebKeyMapper mapper;

    @Override
    public BrebKey save(BrebKey key) {
        return mapper.toDomain(repo.save(mapper.toEntity(key)));
    }

    @Override
    public Optional<BrebKey> findById(String id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<BrebKey> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<BrebKey> findByUserId(String userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<BrebKey> findDefaultForUser(String userId) {
        return repo.findDefaultByUserId(userId)
                .map(mapper::toDomain);
    }

    @Override
    public void clearDefaults(String userId) {
        var keys = repo.findByUserId(userId);

        for (var k : keys) {
            if (k.isDefault()) {
                k.setDefault(false);
                repo.save(k);
            }
        }
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}

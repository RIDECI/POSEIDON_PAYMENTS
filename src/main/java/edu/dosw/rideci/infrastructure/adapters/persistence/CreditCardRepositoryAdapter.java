package edu.dosw.rideci.infrastructure.adapters.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.domain.model.CreditCard;
import edu.dosw.rideci.infrastructure.persistence.Repository.CreditCardJpaRepository;
import edu.dosw.rideci.infrastructure.persistence.Repository.mapper.CreditCardMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreditCardRepositoryAdapter implements CreditCardRepositoryPort {

    private final CreditCardJpaRepository repo;
    private final CreditCardMapper mapper;

    @Override
    public CreditCard save(CreditCard card) {
        return mapper.toDomain(repo.save(mapper.toEntity(card)));
    }

    @Override
    public Optional<CreditCard> findById(String id) {
        return repo.findById(id).map(mapper::toDomain);
    }
    @Override
    public List<CreditCard> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<CreditCard> findByUserId(String userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<CreditCard> findDefaultForUser(String userId) {
        return repo.findDefaultByUserId(userId)
                .map(mapper::toDomain);
    }

     /**
     * Limpia la tarjeta predeterminada actual del usuario.
     */
    @Override
    public void clearDefaults(String userId) {
        var cards = repo.findByUserId(userId);

        for (var c : cards) {
            if (c.isDefault()) {
                c.setDefault(false);
                repo.save(c);
            }
        }
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}

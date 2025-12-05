package edu.dosw.rideci.application.port.out;

import java.util.List;
import java.util.Optional;

import edu.dosw.rideci.domain.model.BrebKey;

public interface BrebKeyRepositoryPort {

    BrebKey save(BrebKey key);

    Optional<BrebKey> findById(String id);

    List<BrebKey> findAll();

    List<BrebKey> findByUserId(String userId);

    Optional<BrebKey> findDefaultForUser(String userId);

    void clearDefaults(String userId);

    void deleteById(String id);
}

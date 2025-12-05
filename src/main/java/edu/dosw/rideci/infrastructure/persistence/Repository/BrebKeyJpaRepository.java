package edu.dosw.rideci.infrastructure.persistence.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dosw.rideci.infrastructure.persistence.Entity.BrebKeyEntity;

public interface BrebKeyJpaRepository
        extends JpaRepository<BrebKeyEntity, String> {

    List<BrebKeyEntity> findByUserId(String userId);

    @Query("SELECT b FROM BrebKeyEntity b WHERE b.userId = :userId AND b.isDefault = true")
    Optional<BrebKeyEntity> findDefaultByUserId(String userId);
}

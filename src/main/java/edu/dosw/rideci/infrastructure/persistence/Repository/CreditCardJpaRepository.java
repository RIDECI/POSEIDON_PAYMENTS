package edu.dosw.rideci.infrastructure.persistence.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dosw.rideci.infrastructure.persistence.Entity.CreditCardEntity;

public interface CreditCardJpaRepository 
        extends JpaRepository<CreditCardEntity, String> {

    List<CreditCardEntity> findByUserId(String userId);
    List<CreditCardEntity> findAll();

    @Query("SELECT c FROM CreditCardEntity c WHERE c.userId = :userId AND c.isDefault = true")
    Optional<CreditCardEntity> findDefaultByUserId(String userId);
}

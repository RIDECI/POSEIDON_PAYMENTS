package edu.dosw.rideci.infrastructure.persistence.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dosw.rideci.infrastructure.persistence.Entity.PaymentMethodEntity;

public interface PaymentMethodJpaRepository
        extends JpaRepository<PaymentMethodEntity, String> {

    List<PaymentMethodEntity> findByUserId(String userId);

    List<PaymentMethodEntity> findAll();

    @Query("SELECT pm FROM PaymentMethodEntity pm WHERE pm.userId = :userId AND pm.isDefault = true")
    Optional<PaymentMethodEntity> findDefaultByUserId(String userId);
}

package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.UpdateSuspensionUseCase;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateSuspensionUseCaseImpl implements UpdateSuspensionUseCase {

    private final PaymentSuspensionRepositoryPort repository;

    @Override
    public PaymentSuspension update(String id, String reason, LocalDateTime expiresAt, String adminId) {
        PaymentSuspension suspension = repository.findById(id)
                .orElseThrow(() -> new RideciBusinessException("Suspension not found: " + id));

        
        if (suspension.getStatus() != null && suspension.getStatus().name().equals("REVOKED")) {
            throw new RideciBusinessException("Cannot update a revoked suspension");
        }

        if (reason != null) suspension.setReason(reason);
        if (expiresAt != null) suspension.setExpiresAt(expiresAt);
        suspension.setUpdatedAt(LocalDateTime.now());
        suspension.setAdminId(adminId);

        return repository.save(suspension);
    }
}

package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.RevokeSuspensionUseCase;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RevokeSuspensionUseCaseImpl implements RevokeSuspensionUseCase {

    private final PaymentSuspensionRepositoryPort repository;

    @Override
    public PaymentSuspension revoke(String id, String adminId) {

        PaymentSuspension suspension = repository.findById(id)
                .orElseThrow(() -> new RideciBusinessException("Suspension not found: " + id));

        if (suspension.getStatus() == SuspensionStatus.REVOKED) {
            throw new RideciBusinessException("Suspension already revoked");
        }

        suspension.setStatus(SuspensionStatus.REVOKED);
        suspension.setUpdatedAt(LocalDateTime.now());
        suspension.setAdminId(adminId);

        return repository.save(suspension);
    }
}

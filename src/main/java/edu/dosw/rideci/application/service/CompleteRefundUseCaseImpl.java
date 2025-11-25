package edu.dosw.rideci.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CompleteRefundUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompleteRefundUseCaseImpl implements CompleteRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public Refund complete(String refundId) {

        Refund refund = refundRepositoryPort.findById(refundId)
                .orElseThrow(() -> new RideciBusinessException("Refund not found: " + refundId));

        if (refund.getStatus() != RefundStatus.APPROVED) {
            throw new RideciBusinessException("Refund must be APPROVED to be completed");
        }

        refund.setStatus(RefundStatus.COMPLETED);
        refund.setCompletedAt(LocalDateTime.now());

        return refundRepositoryPort.save(refund);
    }
}

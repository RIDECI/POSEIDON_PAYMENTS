package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CancelRefundUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelRefundUseCaseImpl implements CancelRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public Refund cancel(String refundId) {

        Refund refund = refundRepositoryPort.findById(refundId)
                .orElseThrow(() -> new RideciBusinessException("Refund not found: " + refundId));

        if (refund.getStatus() == RefundStatus.COMPLETED) {
            throw new RideciBusinessException("Cannot cancel a completed refund");
        }

        if (refund.getStatus() == RefundStatus.CANCELLED) {
            throw new RideciBusinessException("Refund is already cancelled");
        }

        refund.setStatus(RefundStatus.CANCELLED);

        return refundRepositoryPort.save(refund);
    }
}

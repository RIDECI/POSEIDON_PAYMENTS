package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.ApproveRefundUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApproveRefundUseCaseImpl implements ApproveRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public Refund approve(String refundId) {

        Refund refund = refundRepositoryPort.findById(refundId)
                .orElseThrow(() -> new RideciBusinessException("Refund not found: " + refundId));

        if (refund.getStatus() != RefundStatus.PROCESSING) {
            throw new RideciBusinessException("Refund must be PROCESSING to be approved");
        }

        refund.setStatus(RefundStatus.APPROVED);

        return refundRepositoryPort.save(refund);
    }
}

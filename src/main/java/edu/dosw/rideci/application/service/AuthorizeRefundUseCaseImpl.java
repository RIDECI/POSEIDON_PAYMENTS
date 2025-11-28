package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.AuthorizeRefundUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizeRefundUseCaseImpl implements AuthorizeRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public Refund authorize(String refundId) {

        Refund refund = refundRepositoryPort.findById(refundId)
                .orElseThrow(() -> new RideciBusinessException("Refund not found: " + refundId));

        if (refund.getStatus() != RefundStatus.REQUESTED) {
            throw new RideciBusinessException("Only REQUESTED refunds can be authorized");
        }

        refund.setStatus(RefundStatus.AUTHORIZED);

        return refundRepositoryPort.save(refund);
    }
}

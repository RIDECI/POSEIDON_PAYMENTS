package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.ProcessRefundUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessRefundUseCaseImpl implements ProcessRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public Refund process(String refundId) {

        Refund refund = refundRepositoryPort.findById(refundId)
                .orElseThrow(() -> new RideciBusinessException("Refund not found: " + refundId));

        if (refund.getStatus() != RefundStatus.AUTHORIZED) {
            throw new RideciBusinessException("Refund must be AUTHORIZED before processing");
        }

        refund.setStatus(RefundStatus.PROCESSING);

        return refundRepositoryPort.save(refund);
    }
}

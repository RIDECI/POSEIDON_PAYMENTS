package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;

import java.util.List;

public interface GetRefundStatusUseCase {
    List<Refund> findByStatus(RefundStatus status);
}

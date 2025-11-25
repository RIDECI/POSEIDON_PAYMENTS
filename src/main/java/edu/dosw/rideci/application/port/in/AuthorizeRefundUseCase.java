package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Refund;

public interface AuthorizeRefundUseCase {
    Refund authorize(String refundId);
}

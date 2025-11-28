package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.infrastructure.controller.dto.Request.UpdatePaymentRequest;

public interface UpdatePaymentUseCase {
    Transaction updatePartial(String id, UpdatePaymentRequest request);
}

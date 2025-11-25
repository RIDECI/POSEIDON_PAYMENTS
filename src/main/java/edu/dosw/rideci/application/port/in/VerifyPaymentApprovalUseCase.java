package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.Transaction;
import java.util.Optional;

public interface VerifyPaymentApprovalUseCase {
    Optional<Transaction> verifyApproval(String paymentId);
}

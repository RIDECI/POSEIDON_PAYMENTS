package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentVerificationResponse {
    private String paymentId;
    private boolean isApproved;
    private TransactionStatus status;
    private String message;

    public static PaymentVerificationResponse fromDomain(Transaction transaction) {
        if (transaction == null) {
            return PaymentVerificationResponse.builder()
                    .isApproved(false)
                    .message("Payment not found")
                    .build();
        }

        boolean approved = transaction.getStatus() == TransactionStatus.APPROVED 
                        || transaction.getStatus() == TransactionStatus.COMPLETED;

        String message = approved 
                ? "Payment has been approved" 
                : "Payment has not been approved. Current status: " + transaction.getStatus();

        return PaymentVerificationResponse.builder()
                .paymentId(transaction.getId())
                .isApproved(approved)
                .status(transaction.getStatus())
                .message(message)
                .build();
    }
}

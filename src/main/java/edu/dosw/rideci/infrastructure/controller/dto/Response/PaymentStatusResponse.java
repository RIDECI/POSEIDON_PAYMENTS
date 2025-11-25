package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusResponse {
    private String paymentId;
    private TransactionStatus status;
    private String statusDescription;

    public static PaymentStatusResponse fromDomain(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return PaymentStatusResponse.builder()
                .paymentId(transaction.getId())
                .status(transaction.getStatus())
                .statusDescription(getStatusDescription(transaction.getStatus()))
                .build();
    }

    private static String getStatusDescription(TransactionStatus status) {
        if (status == null) {
            return "Unknown";
        }
        return switch (status) {
            case PENDING -> "Payment is pending";
            case AUTHORIZED -> "Payment has been authorized";
            case PROCESSING -> "Payment is being processed";
            case APPROVED -> "Payment has been approved";
            case COMPLETED -> "Payment has been completed successfully";
            case FAILED -> "Payment has failed";
            case REFUNDED -> "Payment has been refunded";
            case PENDING_CASH -> "Payment is pending cash confirmation";
            case CANCELLED -> "Payment has been cancelled";
        };
    }
}

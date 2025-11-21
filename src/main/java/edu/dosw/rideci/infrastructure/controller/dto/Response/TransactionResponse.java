package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.Transaction;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {
    private String id;
    private String bookingId;
    private String passengerId;
    private Double amount;
    private String status;
    private String receiptCode;
    private String errorMessage;

    public static TransactionResponse fromDomain(Transaction t) {
        if (t == null) return null;
        return TransactionResponse.builder()
                .id(t.getId())
                .bookingId(t.getBookingId())
                .passengerId(t.getPassengerId())
                .amount(t.getAmount())
                .status(t.getStatus() != null ? t.getStatus().name() : null)
                .receiptCode(t.getReceiptCode())
                .errorMessage(t.getErrorMessage())
                .build();
    }
}

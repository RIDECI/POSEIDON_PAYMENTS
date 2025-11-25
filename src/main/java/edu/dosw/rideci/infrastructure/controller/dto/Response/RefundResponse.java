package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RefundResponse {

    private String id;
    private String transactionId;
    private String bookingId;
    private String passengerId;
    private Double refundedAmount;
    private RefundStatus status;
    private String reason;
    private LocalDateTime requestAt; // ← FALTABA

    public static RefundResponse fromDomain(Refund r) {
        if (r == null)
            return null;

        return RefundResponse.builder()
                .id(r.getId())
                .transactionId(r.getTransactionId())
                .bookingId(r.getBookingId())
                .passengerId(r.getPassengerId())
                .refundedAmount(r.getRefundedAmount())
                .status(r.getStatus())
                .reason(r.getReason())
                .requestAt(r.getRequestAt()) // ← AGREGADO
                .build();
    }
}

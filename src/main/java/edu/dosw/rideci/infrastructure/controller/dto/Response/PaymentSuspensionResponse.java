package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentSuspensionResponse {
    private String id;
    private String transactionId;
    private String reason;
    private SuspensionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private String adminId;

    public static PaymentSuspensionResponse fromDomain(PaymentSuspension s) {
        if (s == null) return null;
        return PaymentSuspensionResponse.builder()
                .id(s.getId())
                .transactionId(s.getTransactionId())
                .reason(s.getReason())
                .status(s.getStatus())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .expiresAt(s.getExpiresAt())
                .adminId(s.getAdminId())
                .build();
    }
}

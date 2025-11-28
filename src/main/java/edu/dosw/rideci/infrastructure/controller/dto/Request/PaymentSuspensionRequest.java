package edu.dosw.rideci.infrastructure.controller.dto.Request;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentSuspensionRequest {
    private String transactionId;
    private String reason;
    private LocalDateTime expiresAt;
    private String adminId;
}

package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuspension {
    private String id;
    private String transactionId;
    private String reason;
    private SuspensionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private String adminId; 
}

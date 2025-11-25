package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.RefundStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Refund {

    private String id;
    private String transactionId;
    private String bookingId;
    private String passengerId;
    private Double refundedAmount;
    private String reason;
    private RefundStatus status;
    private LocalDateTime requestAt;
    private LocalDateTime completedAt;

}

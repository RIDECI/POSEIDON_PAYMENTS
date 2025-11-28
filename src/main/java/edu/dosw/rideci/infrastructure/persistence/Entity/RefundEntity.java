package edu.dosw.rideci.infrastructure.persistence.Entity;

import edu.dosw.rideci.domain.model.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundEntity {

    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "passenger_id")
    private String passengerId;

    @Column(name = "refunded_amount")
    private Double refundedAmount;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    @Column(name = "external_reference")
    private String externalReference;

    @Column(name = "request_at")
    private LocalDateTime requestAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}

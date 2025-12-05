package edu.dosw.rideci.infrastructure.persistence.Entity;

import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_suspensions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuspensionEntity {

    @Id
    private String id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    private SuspensionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "admin_id")
    private String adminId;
}

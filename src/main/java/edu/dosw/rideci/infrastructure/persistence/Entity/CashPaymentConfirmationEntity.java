package edu.dosw.rideci.infrastructure.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cash_payment_confirmation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashPaymentConfirmationEntity {

    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "passenger_id")
    private String passengerId;

    @Column(nullable = false)
    private Double amount;

    private boolean confirmed;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(columnDefinition = "TEXT")
    private String observations;
}

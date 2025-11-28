package edu.dosw.rideci.infrastructure.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_receipt")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReceiptEntity {

    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "receipt_code", unique = true)
    private String receiptCode;

    @Column(name = "passenger_id")
    private String passengerId;

    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "booking_id")
    private String bookingId;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "transaction_method")
    private String transactionMethod;

    @Column(name = "payment_details", columnDefinition = "TEXT")
    private String paymentDetails;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "download_url")
    private String downloadUrl;
}

package edu.dosw.rideci.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceipt {

    private String id;
    private String transactionId;
    private String receiptCode;
    private String passengerId;
    private String driverId;
    private String bookingId;
    private Double amount;
    private String paymentMethod;
    private String transactionMethod;
    private String paymentDetails;
    private LocalDateTime issuedAt;
    private String downloadUrl;
}

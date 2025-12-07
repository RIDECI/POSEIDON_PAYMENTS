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
public class CashPaymentConfirmation {

    private String id;
    private String transactionId;
    private String bookingId;
    private String driverId;
    private String passengerId;
    private Double amount;
    private boolean confirmed;
    private LocalDateTime confirmedAt;
    private String observations;
}

package edu.dosw.rideci.infrastructure.controller.dto.Response;

import java.time.LocalDateTime;

import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashPaymentConfirmationResponse {

    private String id;
    private String transactionId;
    private String bookingId;  
    private String driverId;
    private String passengerId;
    private Double amount;
    private boolean confirmed;
    private LocalDateTime confirmedAt;
    private String observations;

    public static CashPaymentConfirmationResponse fromDomain(CashPaymentConfirmation c) {
        return CashPaymentConfirmationResponse.builder()
                .id(c.getId())
                .transactionId(c.getTransactionId())
                .bookingId(c.getBookingId()) 
                .driverId(c.getDriverId())
                .passengerId(c.getPassengerId())
                .amount(c.getAmount())
                .confirmed(c.isConfirmed())
                .confirmedAt(c.getConfirmedAt())
                .observations(c.getObservations())
                .build();
    }
}

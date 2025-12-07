package edu.dosw.rideci.infrastructure.controller.dto.Response;

import java.time.LocalDateTime;

import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CashPaymentConfirmationResponse {

    private String id;
    private String transactionId;
    private Double amount;
    private boolean confirmed;
    private LocalDateTime confirmedAt;
    private String driverId;
    private String observations;

    public static CashPaymentConfirmationResponse fromDomain(CashPaymentConfirmation c) {
        return CashPaymentConfirmationResponse.builder()
                .id(c.getId())
                .transactionId(c.getTransactionId())
                .amount(c.getAmount())
                .confirmed(c.isConfirmed())
                .confirmedAt(c.getConfirmedAt())
                .driverId(c.getDriverId())
                .observations(c.getObservations())
                .build();
    }
}

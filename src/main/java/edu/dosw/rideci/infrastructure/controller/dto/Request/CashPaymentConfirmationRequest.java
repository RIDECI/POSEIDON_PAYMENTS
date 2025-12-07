package edu.dosw.rideci.infrastructure.controller.dto.Request;

import lombok.Data;

@Data
public class CashPaymentConfirmationRequest {
    private String driverId;
    private String observations;
}

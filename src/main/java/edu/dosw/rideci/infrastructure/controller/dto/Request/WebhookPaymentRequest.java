package edu.dosw.rideci.infrastructure.controller.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookPaymentRequest {
    private String transactionId;
    private String externalReference;
    private String status;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private String description;
    private String metadata;
}

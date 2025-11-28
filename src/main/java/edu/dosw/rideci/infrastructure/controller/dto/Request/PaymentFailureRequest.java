package edu.dosw.rideci.infrastructure.controller.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailureRequest {
    private String paymentId;
    private String errorMessage;
    private String errorCode;
}

package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.Data;

@Data
public class UpdatePaymentRequest {
    private Double amount;
    private PaymentMethodType paymentMethod;
    private String receiptCode;
    private String extra;
}

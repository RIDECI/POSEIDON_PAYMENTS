package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    private String id;
    private String bookingId;
    private String passengerId;
    private Double amount;
    private PaymentMethodType paymentMethod;
    private String extra;
    private String receiptCode;
    public Transaction toDomain() {
        return Transaction.builder()
                .id(id)
                .bookingId(bookingId)
                .passengerId(passengerId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .extra(extra)
                .receiptCode(receiptCode)
                .build();
    }
}

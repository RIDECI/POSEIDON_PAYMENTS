package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.Transaction;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    private String id;
    private String bookingId;
    private String passengerId;
    private Double amount;

    public Transaction toDomain() {
        return Transaction.builder()
                .id(id)
                .bookingId(bookingId)
                .passengerId(passengerId)
                .amount(amount)
                .build();
    }
}

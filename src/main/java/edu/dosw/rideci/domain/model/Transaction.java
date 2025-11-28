package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private String id;
    private String bookingId;
    private String passengerId;
    private Double amount;
    private TransactionStatus status;
    private String receiptCode;
    private String errorMessage;
    private Integer attempts;
    private LocalDateTime createdAt;
    private PaymentMethodType paymentMethod; 
    private String extra;
}

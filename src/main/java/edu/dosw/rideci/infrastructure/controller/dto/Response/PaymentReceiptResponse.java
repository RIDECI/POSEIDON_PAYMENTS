package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.PaymentReceipt;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentReceiptResponse {

    private String id;
    private String transactionId;
    private String receiptCode;
    private String passengerId;
    private String driverId;
    private String bookingId;
    private Double amount;
    private String paymentMethod;
    private String transactionMethod;
    private String paymentDetails;
    private LocalDateTime issuedAt;
    private String downloadUrl;

    public static PaymentReceiptResponse fromDomain(PaymentReceipt r) {
        return PaymentReceiptResponse.builder()
                .id(r.getId())
                .transactionId(r.getTransactionId())
                .receiptCode(r.getReceiptCode())
                .passengerId(r.getPassengerId())
                .driverId(r.getDriverId())
                .bookingId(r.getBookingId())
                .amount(r.getAmount())
                .paymentMethod(r.getPaymentMethod())
                .transactionMethod(r.getTransactionMethod())
                .paymentDetails(r.getPaymentDetails())
                .issuedAt(r.getIssuedAt())
                .downloadUrl(r.getDownloadUrl())
                .build();
    }
}
